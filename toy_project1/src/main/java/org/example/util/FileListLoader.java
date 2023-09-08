package org.example.util;

import org.example.exeption.FileNotExistException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileListLoader {
    private static String fileFormat;

    private static File[] getFilesFrom(String directoryPath) {
        File directory = new File(directoryPath);
        return directory.listFiles();
    }

    public static List<File> getJsonFiles(String directoryPath) {
        fileFormat = ".json";
        return getFiles(directoryPath);
    }

    public static List<File> getCsvFiles(String directoryPath) {
        fileFormat = ".csv";
        return getFiles(directoryPath);
    }

    private static List<File> getFiles(String directoryPath) {
        File[] wholeFiles = getFilesFrom(directoryPath);
        List<File> files = new ArrayList<>();

        if (wholeFiles.length == 0)
            throw new NullPointerException("Any files doesn't exist in this directory. Please confirm base path."); // TODO: magic number

        for (File file : wholeFiles) {
            if (file.isFile() && file.getName().endsWith(fileFormat))
                files.add(file);
        }

        try {
            Verifier.validExistenceOf(files);
        } catch (FileNotExistException e) {
            e.printStackTrace();
        }

        return files;
    }

    public static int getNumberFromLastFiles(String directoryPath) {
        File[] files = getFilesFrom(directoryPath);

        List<String> fileNames = null;
        if (files != null) {
            fileNames = new ArrayList<>();

            Pattern pattern = Pattern.compile("trip_(\\d+)\\.json"); // TODO: magic number
            for (File file : files) {
                if (file.isFile()) {
                    Matcher matcher = pattern.matcher(file.getName());
                    if (matcher.find()) {
                        fileNames.add(file.getName());
                    }
                }
            }

            fileNames.sort((fileName1, fileName2) -> {
                Matcher matcher1 = pattern.matcher(fileName1);
                Matcher matcher2 = pattern.matcher(fileName2);
                if (matcher1.find() && matcher2.find()) {
                    int number1 = Integer.parseInt(matcher1.group(1)); // TODO: magic number
                    int number2 = Integer.parseInt(matcher2.group(1)); // TODO: magic number
                    return Integer.compare(number1, number2);
                }
                return fileName1.compareTo(fileName2);
            });
        }
        Pattern patternForExtractedNumber = Pattern.compile("\\d+"); // TODO: magic number

        assert fileNames != null;
        int numberOfLastFile = 0; // TODO: magic number
        int SizeOfFileNamesList = fileNames.size();
        if (SizeOfFileNamesList >= 1) { // TODO: magic number
            Matcher matcherNumberOfLastFile = patternForExtractedNumber.matcher(fileNames.get(fileNames.size() - 1)); // TODO: magic number
            while (matcherNumberOfLastFile.find()) {
                numberOfLastFile = Integer.parseInt(matcherNumberOfLastFile.group()); // 숫자로 변환
            }
        }

        return numberOfLastFile;
    }
}
