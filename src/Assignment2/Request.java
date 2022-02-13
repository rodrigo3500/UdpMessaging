package Assignment2;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Request {
    private final byte[] request;
    private int maxRequestSize;

    public Request() {
        this.request = "Invalid".getBytes();
    }

    public Request(String data, int maxRequestSize){
        this.maxRequestSize = maxRequestSize;
        request = data.getBytes();
    }

    public Request(String fileName, boolean writeRequest, String mode, int maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
        byte[] modeBytes = mode.getBytes();
        byte[] fileNameBytes = fileName.getBytes();
        int requestLength = 2 + fileNameBytes.length + 1 +  modeBytes.length + 1 ;
        this.request = new byte[requestLength];
        // Set first two byte, 01 if read , 02 if write
        this.request[0] = 0;
        if(writeRequest){
            this.request[1] = 2;
        } else {
            this.request[1] = 1;
        }
        // Set the file name bytes
        int i = 2;
        for (byte b: fileNameBytes){
            request[i] = b;
            i++;
        }
        // Add zero
        request[2+ fileNameBytes.length] = 0;

        // Set the mode bytes
        int j = 3 +  fileNameBytes.length;
        for (byte b: modeBytes){
            request[j] = b;
            j++;
        }
        // Set last byte to 0
        request[requestLength-1] = 0;
    }

    /**
     * Validates the request message header, message, and mode type
     * @return Boolean true if request is valid, false otherwise
     */
    public boolean validateRequestString(){
        System.out.println("Validating Request ...");
        System.out.println("Max Request Size: " + this.maxRequestSize);
        // Validate header (read = 01 , write = 02)
        if (!(request[0] == 0 && (request[1]==1 || request[1] == 2))){
            System.out.println("Invalid request error: Incorrect header format");
            return false;
        }
        System.out.println("Request Header validated");

        int fileNameEnd = 0;

        for (int i = 2; i < this.maxRequestSize; i++) {
            if (request[i] == 0){
                fileNameEnd = i;
                break;
            }
        }

        if (fileNameEnd==0){
            System.out.println("File name exceeded byte limit of: " + this.maxRequestSize);
            return false;
        }


        byte[] fileNameBytes = Arrays.copyOfRange(request, 2, fileNameEnd);
        String fileNameString = new String(fileNameBytes, StandardCharsets.UTF_8);
        System.out.println("Validated File Name: \"" + fileNameString + "\"");

        int modeTypeEnd = 0;
        for (int i = fileNameEnd + 1 ; i < this.maxRequestSize; i++) {
            if (request[i] == 0){
                modeTypeEnd = i;
                break;
            }
        }
        if (modeTypeEnd==0){
            System.out.println("Mode type exceeded byte limit of: " + this.maxRequestSize);
            return false;
        }

        byte[] modeNameBytes = Arrays.copyOfRange(request, fileNameEnd + 1, modeTypeEnd);
        String modeNameString = new String(modeNameBytes, StandardCharsets.UTF_8);

        // Validate the mode type and return true if it passes, false otherwise
        if(modeNameString.equalsIgnoreCase("octet") || modeNameString.equalsIgnoreCase("netascii")){
            System.out.println("Validated Mode Type: \"" + modeNameString + "\"");
            System.out.println(" * Message Validation Success *\n");

            return true;
        } else {
            System.out.println("Invalid Mode Type: \"" + modeNameString + "\"");
            System.out.println(" * Message Validation FAILURE *\n");
            return false;
        }
    }

    public String getRequestString() {
        return new String(request);
    }

    public String requestType(){
        if(this.request[1] == 1){
            return "read";
        } else if (this.request[1]== 2) {
            return "write";
        } else {
            return "invalid";
        }
    }
}
