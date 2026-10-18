package mg.itu.framework.util;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.framework.model.MapFile;

import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadUtil {


    public static MapFile buildFile(HttpServletRequest req, String fileName) throws Exception {
        return buildFile(req, fileName, null);
    }

    public static MapFile buildFile(HttpServletRequest req, String fileName, String pathFile) throws Exception {
        Part part = req.getPart(fileName);

        return buildFile(part, pathFile);
    }

    public static MapFile[] buildFile(HttpServletRequest req, String fileName, boolean multiple) throws Exception {
        return buildFile(req, fileName, null, multiple);
    }

    public static MapFile[] buildFile(HttpServletRequest req, String fileName, String pathFile, boolean multiple) throws Exception {
        if (!multiple) {
            MapFile file = buildFile(req, fileName, pathFile);
            return file == null ? new MapFile[0] : new MapFile[] { file };
        }

        List<MapFile> files = new ArrayList<>();
        for (Part part : req.getParts()) {
            if (fileName.equals(part.getName())) {
                MapFile file = buildFile(part, pathFile);
                if (file != null) {
                    files.add(file);
                }
            }
        }

        return files.toArray(new MapFile[0]);
    }

    private static MapFile buildFile(Part part, String pathFile) throws Exception {
        if (part == null || part.getSize() == 0) {
            return null;
        }

        String submittedFileName = part.getSubmittedFileName();
        if (submittedFileName == null || submittedFileName.isEmpty()) {
            return null;
        }

        String name = submittedFileName;
        String extension = "";

        int lastDot = submittedFileName.lastIndexOf('.');
        if (lastDot >= 0) {
            name = submittedFileName.substring(0, lastDot);
            extension = submittedFileName.substring(lastDot + 1);
        }

        byte[] content;
        try (InputStream input = part.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            content = output.toByteArray();
        }

        MapFile file = new MapFile(name, extension, content);
        file.setPath(pathFile);
        return file;
    }

}
