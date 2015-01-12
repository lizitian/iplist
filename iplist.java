import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
class iplist {
    public static void main(String[] args) {
        String data = getRawData();
        String[] ip4records = parseIPv4Records(data);
        for(String i : ip4records)
            System.out.println(i);
    }
    private static String[] parseIPv4Records(String data) {
        List<String> list = new ArrayList<String>();
        String re = "(?<record>[a-z]*\\|[A-Z]{2}\\|ipv4\\|(\\d{1,3}.){3}\\d{1,3}\\|\\d*\\|\\d{8}\\|(allocated|assigned))";
        Matcher m = Pattern.compile(re).matcher(data);
        while(m.find())
            list.add(m.group("record"));
        return list.toArray(new String[0]);
    }
    private static String getRawData() {
        String urls[] = {"http://ftp.afrinic.net/pub/stats/afrinic/delegated-afrinic-extended-latest",
            "http://ftp.apnic.net/pub/stats/apnic/delegated-apnic-extended-latest",
            "http://ftp.arin.net/pub/stats/arin/delegated-arin-extended-latest",
            "http://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-extended-latest",
            "http://ftp.ripe.net/pub/stats/ripencc/delegated-ripencc-extended-latest"};
        String data = "";
        for(String i : urls) {
            System.err.println("Downloading " + i + ".");
            data = data + download(i) + "\n";
        }
        return data;
    }
    private static String download(String url) {
        URL download_url;
        String result;
        try {
            download_url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            result = downloadFromURL(download_url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
    private static String downloadFromURL(URL url) throws IOException {
        String result;
        byte buffer[] = new byte[10000];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.err.print("Connecting...");
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        int total = conn.getContentLength();
        int length, sum = 0;
        String[] url_paths = url.getFile().split("/");
        String filename = url_paths[url_paths.length - 1];
        while((length = is.read(buffer)) != -1) {
            sum += length;
            String status;
            if(total == -1)
                status = sum + "/unknown";
            else
                status = sum * 100 / total + "% ... " + sum + "/" + total;
            System.err.print("\r" + filename + " ... " + status);
            baos.write(buffer, 0, length);
        }
        is.close();
        result = baos.toString();
        if((sum == total || total == -1) && result.length() == sum && sum > 0)
            System.err.print(" ... OK.\n");
        else
            System.err.print(" ... WARNING: length mismatch.\n");
        return result;
    }
}
