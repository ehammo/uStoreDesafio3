package uploadService;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.nio.file.Paths;

/**
 * Created by eduardo on 15/07/2017.
 */
@Path("/upload")
public class FileUploadService {
    public final String UPLOAD_FOLDER = "C:/uploads/";
    /** The path to the folder where we want to store the uploaded files */
    public FileUploadService() {
    }
    @Context
    private UriInfo context;
    /**
     * Returns text response to caller containing uploaded file location
     *
     * @return error response in case of missing parameters an internal
     *         exception or success response if file has been stored
     *         successfully
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
        // check if all form parameters are provided
        if (uploadedInputStream == null || fileDetail == null) {
            System.out.println("erro1");
            System.out.println("upload: "+uploadedInputStream.toString());
            System.out.println("fileDetail: "+fileDetail);
            return Response.status(400).entity("Invalid form data").build();
        }
        // create our destination folder, if it not exists

        try {
            createFolderIfNotExists(UPLOAD_FOLDER);
        } catch (Exception se) {
            System.out.println("erro2");
            System.out.println("upload: "+uploadedInputStream.toString());
            System.out.println("fileDetail: "+fileDetail);
            return Response.status(500)
                    .entity("Can not create destination folder on server")
                    .build();
        }
        String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
        System.out.println("myUploadFolder: "+uploadedFileLocation);
        try {
            saveToFile(uploadedInputStream, uploadedFileLocation);
        } catch (IOException e) {
            System.out.println("erro3");
            System.out.println("upload: "+uploadedInputStream.toString());
            System.out.println("fileDetail: "+fileDetail);
            System.out.println(e.getMessage());
            return Response.status(500).entity("Can not save file").build();
        }
        return Response.status(200)
                .entity("File saved to " + uploadedFileLocation).build();
    }
    /**
     * Utility method to save InputStream data to target location/file
     *
     * @param inStream
     *            - InputStream to be saved
     * @param target
     *            - full path to destination file
     */
    private void saveToFile(InputStream inStream, String target)
            throws IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];
        System.out.println("trying to save: "+target);
        out = new FileOutputStream(new File(target));
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }
    /**
     * Creates a folder to desired location if it not already exists
     *
     * @param dirName
     *            - full path to the folder
     * @throws SecurityException
     *             - in case you don't have permission to create the folder
     */
    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }
}