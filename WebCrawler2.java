import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WebCrawler2{
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        //java.io.PrintWriter pw = new java.io.PrintWriter("css/");
        System.out.print("Enter a URL: ");
        String url = input.nextLine();
        File css = new File("css");
        boolean del = false;
        if(css.exists()) del = css.delete();
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
        //System.out.println(urlname);
        int currentcss = line.indexOf("href=\"");
        boolean del = false;
        if(currentcss > 0) {
            int endIndex = line.indexOf(".css");
            if(endIndex > 0) {
                String css = line.substring(currentcss, endIndex);

                //String urlcss = urlname.substring(currentcss,endIndex) + css + ".css/";
                css = css.replaceAll("href=\"", "");
                css = css + ".css/";
                css = urlname.substring(urlname.indexOf("http://"),urlname.indexOf("/")) + css;
                //css = css.replaceAll("href=\"", "");
                System.out.println(css);
                //System.out.println("Urlclass: " + urlcss);
                //System.out.println("Done");
            }
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
                if (urlname != null) getCss(line,urlname);

            }
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return list;
    }
}
