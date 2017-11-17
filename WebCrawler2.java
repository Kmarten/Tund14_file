import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class WebCrawler2{
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        //java.io.PrintWriter pw = new java.io.PrintWriter("css/");
        System.out.print("Enter a URL: ");
        String url = input.nextLine();
        File css = new File("src/css");
        boolean del = false;
        if(css.exists()) del = css.delete();
        System.out.println(del);
        if(!css.mkdirs()) {
            System.out.println("Error creating folder");
        }
        crawler(url); // Traverse the web from the starting url
    }

    public static void crawler(String startingURL) {
        ArrayList<String> listOfPendingURLs = new ArrayList<>();
        ArrayList<String> listOfTraversedURLs = new ArrayList<>();

        listOfPendingURLs.add(startingURL);
        while (!listOfPendingURLs.isEmpty() &&
                listOfTraversedURLs.size() <= 50) {
            String urlString = listOfPendingURLs.remove(0);
            if (!listOfTraversedURLs.contains(urlString)) {
                listOfTraversedURLs.add(urlString);
                System.out.println("Craw " + urlString);

                for (String s: getSubURLs(urlString)) {
                    if (!listOfTraversedURLs.contains(s))
                        listOfPendingURLs.add(s);
                }
            }
        }
    }
    public static void getCss(String line, String urlname) {
        int currentcss = line.indexOf("href=\"");
        boolean del = false;
        if(currentcss > 0) {
            int endIndex = line.indexOf(".css\"");
            String css = null;
            if(endIndex > 0) {
                css = line.substring(currentcss, endIndex);
                //System.out.println(css);
                String normurl = urlname.substring(currentcss, urlname.indexOf("\""));
                css = css.replaceAll("href=\"/", "");
                try {

                    System.out.println("Try block");
                    URL cssurl = new URL("http://" + normurl + "/" + css + ".css\"");
                    System.out.println(cssurl);
                    //System.out.println("here");
                } catch (MalformedURLException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Error in catch");
                }
            }
            //System.out.println(css);
            //File tmp = new File("src/css/"+urlname);

            /*if(tmp.exists()) del = tmp.delete();
            if(!tmp.mkdirs()) {
                System.out.println("getCss:Error creating folder");
            }*/
        }
    }
    public static ArrayList<String> getSubURLs(String urlString) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        try {
            java.net.URL url = new java.net.URL(urlString);
            Scanner input = new Scanner(url.openStream());
            int current = 0;
            int currentcss = 0;
            String urlname = null;
            while (input.hasNext()) {
                String line = input.nextLine();
                current = line.indexOf("http:", current);
                while (current > 0) {
                    int endIndex = line.indexOf("\"", current);
                    if (endIndex > 0) { // Ensure that a correct URL is found
                        urlname = line.substring(current, endIndex);
                        list.add(urlname);
                        current = line.indexOf("http:", endIndex);
                    }
                    else
                        current = -1;
                }
                getCss(line,urlname);

            }
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return list;
    }
}
