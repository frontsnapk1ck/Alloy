package frontsnapk1ck.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    /**
     * <p>takes in a given path and if the file exits, it will read it
     * and take all the lines in that file and split them into a separate 
     * string</p>
     * <p>example: File contents: <blockquote><pre>
     * i love java
     * it is my favorite programing language
     * i like it a lot
     * 
     * 1
     * 2
     * 3
     * </pre></blockquote><p></p>
     * Return contents:
     * <blockquote><pre>
     * String[] {
     *    "i love java" ,
     *    "it is my favorite programing language" ,
     *    "i like it a lot" ,
     *    "" ,
     *    "1" ,
     *    "2" ,
     *    "3" ,
     * }
     * * </pre></blockquote><p></p>
     * @see FileReader#read(File)
     * @param path the path of the file that is being read from
     * @return an array of strings split at newlines in the given
     *          file
     */
    public static String[] read(String path) 
    {
        try 
        {
            return read(new File(path));
        }
        catch (Exception e) 
        {
            return null;
        }
    }

    /**
     * <p>takes in a given path and if the file exits, it will read it
     * and take all the lines in that file and split them into a separate 
     * string</p>
     * <p>example: File contents: <blockquote><pre>
     * i love java
     * it is my favorite programing language
     * i like it a lot
     * 
     * 1
     * 2
     * 3
     * </pre></blockquote><p></p>
     * Return contents:
     * <blockquote><pre>
     * String[] {
     *    "i love java" ,
     *    "it is my favorite programing language" ,
     *    "i like it a lot" ,
     *    "" ,
     *    "1" ,
     *    "2" ,
     *    "3" ,
     * }
     * * </pre></blockquote><p></p>
     * 
     * @param file the file that is being read from
     * @return an array of strings split at newlines in the given
     *          file
     */
    public static String[] read( File file) 
    {
        try {
            Scanner s = new Scanner(file);
            List<String> data = new ArrayList<String>();
            
            while (s.hasNextLine())
                data.add( s.nextLine() );

            s.close();

            Object[] arr = data.toArray();
            String[] sArr = new String[arr.length]; 
            
            for (int i = 0; i < sArr.length; i++) 
            {
                sArr[i] = (String)arr[i];  
            }

            return sArr;

        } catch (IOException e) {
            System.err.println("error reading " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }
    

    /**
     * <p>takes in a given folder path, and provided that the path specified
     * was a folder, it will list all of the items in that folder.</p>
     * <p>example: Folder contents: <blockquote><pre>
     * TOP_FOLDER
     * |_File1.text
     * |_File2.json
     * |_config.yml
     * |_sub_folder
     * | |_subFile1.txt
     * | |_subFile2.txt
     * |_control.xml
     * </pre></blockquote><p></p>
     * Return contents:
     * <blockquote><pre>
     * String[] {
     *   "File1.text" ,
     *   "File2.json" ,
     *   "config.yml" ,
     *   "sub_folder/" ,
     *   "control.xml" ,
     * }
     * </pre></blockquote><p></p>
     * @see FileReader#readFolderContents(File)
     * @param path the path with which you would like to read
     * @return  null is the file doesn't exit or it is not a folder 
     *          otherwise it returns list of all the filenames in the 
     *          that folder
     */
    public static String[] readFolderContents (String path)
    {
        try 
        {
            return readFolderContents(new File(path));
        }
        catch (Exception e) 
        {
            return null;
        }
    }

    /**
     * <p>takes in a given folder path, and provided that the path specified
     * was a folder, it will list all of the items in that folder.</p>
     * <p>example: Folder contents: <blockquote><pre>
     * TOP_FOLDER
     * |_File1.text
     * |_File2.json
     * |_config.yml
     * |_sub_folder
     * | |_subFile1.txt
     * | |_subFile2.txt
     * |_control.xml
     * </pre></blockquote><p></p>
     * Return contents:
     * <blockquote><pre>
     * String[] {
     *   "File1.text" ,
     *   "File2.json" ,
     *   "config.yml" ,
     *   "sub_folder/" ,
     *   "control.xml" ,
     * }
     * </pre></blockquote><p></p>
     * 
     * @param file the file with which you would like to read
     * @return  null is the file doesn't exit or it is not a folder 
     *          otherwise it returns list of all the filenames in the 
     *          that folder
     */
    public static String[] readFolderContents(File file) 
    {
        if (!file.exists())
            return null;
        if (!file.isDirectory())
            return null;

        String[] arr = new String[file.listFiles().length];
        int i = 0;
        for (File sf : file.listFiles())
        {
            String name = sf.getName();
            name.replace(file.getAbsolutePath(), "");
            arr[i] = name;
            i++;
        }
        return arr;
	}
}