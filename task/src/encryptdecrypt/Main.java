package encryptdecrypt;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        String option = "enc";
        String data = "";
        int key = 0;
        String outFile = "";
        String inFile = "";
        String alg = "shift";

        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-mode":
                    option = args[i + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    break;
                case "-data":
                    data = args[i + 1];
                    break;
                case "-in":
                    inFile = args[i + 1];
                    break;
                case "-out":
                    outFile = args[i + 1];
                    break;
                case "-alg":
                    alg = args[i + 1];
                    break;
            }
        }

        CryptographyMechanism cryptographyMechanism = null;

        if (option.equals("enc")) {
            if (alg.equals("unicode")) {
                cryptographyMechanism = new UnicodeEncryption(inFile, outFile, data, key);
            } else {
                cryptographyMechanism = new ShiftEncryption(inFile, outFile, data, key);
            }
        } else {
            if (alg.equals("unicode")) {
                cryptographyMechanism = new UnicodeDecryption(inFile, outFile, data, key);
            } else {
                cryptographyMechanism = new ShiftDecryption(inFile, outFile, data, key);
            }
        }

        cryptographyMechanism.solve();

    }
}

abstract class CryptographyMechanism {

    String inFile;
    String outFile;
    String data;
    int key;

    public CryptographyMechanism(String inFile, String outFile, String data, int key) {
        this.inFile = inFile;
        this.outFile = outFile;
        this.data = data;
        this.key = key;
    }

    public void solve() {
        getData();
        crypt();
        writeData();
    }

    public void getData() {
        if (data.length() == 0 && inFile.length() != 0) {
            try (FileReader fileReader = new FileReader(inFile)) {
                StringBuilder builder = new StringBuilder();
                while (fileReader.ready()) {
                    builder.append((char) fileReader.read());
                }
                data = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void crypt();

    public void writeData() {
        if (outFile.length() != 0) {
            try (FileWriter fileWriter = new FileWriter(outFile)) {
                fileWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(data);
        }
    }
}

class ShiftEncryption extends CryptographyMechanism {

    public ShiftEncryption(String inFile, String outFile, String data, int key) {
        super(inFile, outFile, data, key);
    }

    @Override
    public void crypt() {
        StringBuilder encryptedMessage = new StringBuilder();
        for (char c : data.toCharArray()) {
            encryptedMessage.append(encryptLetter(c));
        }
        data = encryptedMessage.toString();
    }

    private char encryptLetter(char c) {
        if (!Character.isAlphabetic(c)) {
            return c;
        }
        if (c >= 65 && c <= 90) {
            return (char) (c + key > 90 ? c + key - 26 : c + key);
        } else {
            return (char) (c + key > 122 ? c + key - 26 : c + key);
        }
    }


}

class ShiftDecryption extends CryptographyMechanism {

    public ShiftDecryption(String inFile, String outFile, String data, int key) {
        super(inFile, outFile, data, key);
    }

    @Override
    public void crypt() {
        StringBuilder decryptedMessage = new StringBuilder();
        for (char c : data.toCharArray()) {
            decryptedMessage.append(decryptLetter(c));
        }
        data = decryptedMessage.toString();
    }

    private char decryptLetter(char c) {
        if (!Character.isAlphabetic(c)) {
            return c;
        }

        if (c >= 65 && c <= 90) {
            return (char) (c - key < 65 ? c - key + 26 : c - key);
        } else {
            return (char) (c - key < 97 ? c - key + 26 : c - key);
        }
    }
}

class UnicodeEncryption extends CryptographyMechanism {

    public UnicodeEncryption(String inFile, String outFile, String data, int key) {
        super(inFile, outFile, data, key);
    }

    @Override
    public void crypt() {
        StringBuilder encryptedMessage = new StringBuilder();
        for (char c : data.toCharArray()) {
            encryptedMessage.append((char) (c + key));
        }
        data = encryptedMessage.toString();
    }
}


class UnicodeDecryption extends CryptographyMechanism {

    public UnicodeDecryption(String inFile, String outFile, String data, int key) {
        super(inFile, outFile, data, key);
    }

    @Override
    public void crypt() {
        StringBuilder decryptedMessage = new StringBuilder();
        for (char c : data.toCharArray()) {
            decryptedMessage.append((char) (c - key));
        }
        data = decryptedMessage.toString();
    }
}