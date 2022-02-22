package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    /*
    Markdown to html converter from input file to output
    input file name in args[0] && output file name in args[1]
     */
    public static void main(String ... args) {
        Paragraph para;
        try(var in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8));
        var out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
            String line;
            var sb = new StringBuilder();
            int level = 0;
            boolean isStartOfPara = true;
            do {
                line = in.readLine();
                if (line == null || line.isEmpty()) {
                    para = new Paragraph(level > 0 ? sb.substring(level + 1) : sb.toString(), level); //create paragraph with specified level
                    if (!sb.isEmpty()) {
                        out.append(para.toHtml()).append("\n");
                    }
                    level = 0;
                    sb = new StringBuilder();
                    isStartOfPara = true;
                } else {
                    sb.append(line).append("\n");
                    if (isStartOfPara && line.startsWith("#")) {
                        int lvl;
                        for(lvl = 0; lvl < line.length() && line.charAt(lvl) == '#'; ++lvl);
                        level = Character.isWhitespace(line.charAt(lvl)) ? lvl : 0;
                        isStartOfPara = false;
                    }
                }
            } while(line != null);
        } catch (FileNotFoundException e) {
            System.out.println("File is not found");
        } catch (IOException e) {
            System.out.println("IOException occurred in your program");
        }
    }
}
