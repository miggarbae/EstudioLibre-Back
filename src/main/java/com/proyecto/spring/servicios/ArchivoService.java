package com.proyecto.spring.servicios;

import com.proyecto.spring.modelos.Archivo;
import com.proyecto.spring.modelos.Usuario;
import com.proyecto.spring.repositorios.ArchivoRepository;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArchivoService {
    
    private final ArchivoRepository archivoRepository;

    // 🔥 Inyección del repositorio en el constructor
    public ArchivoService(ArchivoRepository archivoRepository) {
        this.archivoRepository = archivoRepository;
    }

    private static final List<String> TIPOS_PERMITIDOS = List.of(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "text/plain",
        "application/rtf"
    );

    public Archivo guardarArchivo(MultipartFile archivo, Usuario usuario) throws IOException {
        String tipo = archivo.getContentType();
        
        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF, Word, TXT y RTF.");
        }

        Archivo nuevoArchivo = new Archivo();
        nuevoArchivo.setNombre(archivo.getOriginalFilename());
        nuevoArchivo.setTipo(tipo);
        nuevoArchivo.setDatos(archivo.getBytes());
        nuevoArchivo.setUsuario(usuario);
        
        return archivoRepository.save(nuevoArchivo);
    }

    public byte[] convertirArchivoAPDF(Archivo archivo) throws IOException {
        if (archivo.getTipo().equals("application/pdf")) {
            return archivo.getDatos();  // ✅ Si ya es PDF, no hace falta convertirlo
        }
    
        String contenido = extraerTexto(archivo);  // 🔍 Extraer texto del archivo
        System.out.println("Texto extraído: \n" + contenido); // 📌 Debug para verificar si el contenido se extrae bien
    
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
    
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.setLeading(14.5f); // 🔥 Ajustar interlineado
            contentStream.newLineAtOffset(50, 750); // 📍 Ajustar la posición inicial
    
            String[] lineas = contenido.split("\n"); // ✂️ Separar el texto en líneas
            for (String linea : lineas) {
                contentStream.showText(linea); 
                contentStream.newLine(); // ⬇️ Saltar línea para evitar sobreescribir
            }
    
            contentStream.endText();
        }
    
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
    
        return outputStream.toByteArray();
    }    

    private String extraerTexto(Archivo archivo) throws IOException {
        if (archivo.getTipo().equals("text/plain")) {
            return new String(archivo.getDatos()); // ✅ Archivos TXT
        } 
        else if (archivo.getTipo().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            // 📝 Extraer texto de archivos DOCX (Word moderno)
            try (InputStream inputStream = new ByteArrayInputStream(archivo.getDatos());
                 XWPFDocument doc = new XWPFDocument(inputStream)) {
                StringBuilder texto = new StringBuilder();
                for (XWPFParagraph parrafo : doc.getParagraphs()) {
                    texto.append(parrafo.getText()).append("\n"); // 📝 Agregar salto de línea
                }
                return texto.toString().trim(); // 🔥 Eliminar espacios innecesarios
            }
        } 
        else if (archivo.getTipo().equals("application/msword")) {
            // 📄 Extraer texto de archivos DOC (Word antiguo)
            try (InputStream inputStream = new ByteArrayInputStream(archivo.getDatos());
                 HWPFDocument doc = new HWPFDocument(inputStream)) {
                WordExtractor extractor = new WordExtractor(doc);
                return extractor.getText().trim();
            }
        } 
        else if (archivo.getTipo().equals("application/rtf")) {
            return new String(archivo.getDatos());  // ✅ Archivos RTF (simple)
        }
        return "";
    }    

    public Optional<Archivo> obtenerArchivo(Long id) {
        return archivoRepository.findById(id);
    }

    public List<Archivo> obtenerArchivosPorUsuario(Long usuarioId) {
        return archivoRepository.findByUsuarioId(usuarioId);
    }
}
