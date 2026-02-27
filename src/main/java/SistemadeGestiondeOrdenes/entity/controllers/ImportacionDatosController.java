package SistemadeGestiondeOrdenes.entity.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador REST específico para la importación de datos.
 * Maneja la subida de archivos Excel y CSV para importación.
 *
 * @RestController Marca esta clase como un controlador REST
 * @RequestMapping Define la ruta base para todos los endpoints
 */
@RestController
@RequestMapping("/api")
public class ImportacionDatosController {

    private static final Logger log = LoggerFactory.getLogger(ImportacionDatosController.class);

    private static final String UPLOAD_DIR = "uploads";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_EXTENSIONS = {".xlsx", ".xls", ".csv"};

    /**
     * Endpoint específico para subir archivos de importación de datos.
     * Acepta archivos Excel (.xlsx, .xls) y CSV (.csv).
     *
     * @param file Archivo a subir (MultipartFile)
     * @return ResponseEntity con información del archivo subido
     */
    @PostMapping("/upload-import-file")
    public ResponseEntity<?> uploadImportFile(@RequestParam("file") MultipartFile file) {
        try {
            // Validar que el archivo no esté vacío
            if (file.isEmpty()) {
                log.warn("Intento de subir archivo vacío");
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("El archivo está vacío"));
            }

            // Validar el tamaño del archivo
            if (file.getSize() > MAX_FILE_SIZE) {
                log.warn("Archivo demasiado grande: {} bytes", file.getSize());
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("El archivo es demasiado grande. Tamaño máximo: 10MB"));
            }

            // Validar el tipo de archivo
            String fileName = file.getOriginalFilename();
            if (fileName == null || !isValidFileType(fileName)) {
                log.warn("Tipo de archivo no válido: {}", fileName);
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Tipo de archivo no válido. Solo se permiten archivos .xlsx, .xls, .csv"));
            }

            // Crear el directorio de uploads si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Directorio de uploads creado: {}", uploadPath.toAbsolutePath());
            }

            // Limpiar el nombre del archivo
            String cleanFileName = StringUtils.cleanPath(fileName);
            
            // Generar un nombre único para evitar conflictos
            String uniqueFileName = System.currentTimeMillis() + "_" + cleanFileName;
            Path filePath = uploadPath.resolve(uniqueFileName);

            // Guardar el archivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("Archivo de importación subido exitosamente: {}", uniqueFileName);

            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Archivo subido exitosamente");
            response.put("fileName", uniqueFileName);
            response.put("originalFileName", cleanFileName);
            response.put("fileSize", file.getSize());
            response.put("filePath", filePath.toString());
            response.put("uploadDirectory", uploadPath.toAbsolutePath().toString());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("Error al guardar el archivo de importación: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error al guardar el archivo: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al procesar el archivo de importación: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Valida si el tipo de archivo es permitido para importación.
     *
     * @param fileName Nombre del archivo
     * @return true si el archivo es válido, false en caso contrario
     */
    private boolean isValidFileType(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        for (String extension : ALLOWED_EXTENSIONS) {
            if (lowerCaseFileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crea una respuesta de error estandarizada.
     *
     * @param message Mensaje de error
     * @return Mapa con la respuesta de error
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        response.put("timestamp", new Date());
        return response;
    }

    /**
     * Lista todos los archivos en el directorio de uploads.
     *
     * @return ResponseEntity con la lista de archivos
     */
    @GetMapping("/import-files")
    public ResponseEntity<?> listImportFiles() {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Map<String, Object> response = new HashMap<>();
                response.put("files", new String[0]);
                response.put("message", "No hay archivos de importación");
                response.put("count", 0);
                return ResponseEntity.ok(response);
            }

            String[] files = Files.list(uploadPath)
                .map(path -> path.getFileName().toString())
                .filter(name -> isValidFileType(name))
                .toArray(String[]::new);

            Map<String, Object> response = new HashMap<>();
            response.put("files", files);
            response.put("count", files.length);
            response.put("uploadDirectory", uploadPath.toAbsolutePath().toString());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("Error al listar archivos de importación: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error al listar archivos: " + e.getMessage()));
        }
    }
}
