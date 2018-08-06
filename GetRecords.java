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
class GetRecords
{
    private static String rirs = "afrinic|apnic|arin|lacnic|ripencc";
    public static Record[] get() {
        String data = getRawData();
        List<Record> list = new ArrayList<Record>();
        Record record;
        String re = "(" + rirs + ")\\|(?<cc>[A-Z]{2})\\|ipv4\\|(?<start1>\\d{1,3})\\.(?<start2>\\d{1,3})\\.(?<start3>\\d{1,3})\\.(?<start4>\\d{1,3})\\|(?<value>\\d+)\\|\\d{8}\\|(allocated|assigned)";
        Matcher m = Pattern.compile(re).matcher(data);
        while(m.find()) {
            record = new Record();
            record.cc = m.group("cc");
            record.start = parseIP(m.group("start1"), m.group("start2"), m.group("start3"), m.group("start4"));
            record.value = parseUInt(m.group("value"));
            list.add(record);
        }
        return list.toArray(new Record[0]);
    }
    private static String getRawData() {
        String data = "";
        for(String i : getRIRURLList()) {
            System.err.println("Downloading " + i);
            data = data + download(i) + "\n";
        }
        return data;
    }
    private static String[] getRIRURLList() {
        List<String> list = new ArrayList<String>();
        for(String i : rirs.split("\\|"))
            list.add((i.equals("apnic") ? "http" : "https") + "://ftp." + (i.equals("ripencc") ? "ripe" : i) + ".net/pub/stats/" + i + "/delegated-" + i + "-extended-latest");
        return list.toArray(new String[0]);
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
    private static int parseIP(String ip1, String ip2, String ip3, String ip4) {
        int ip_num1 = parseUInt(ip1);
        int ip_num2 = parseUInt(ip2);
        int ip_num3 = parseUInt(ip3);
        int ip_num4 = parseUInt(ip4);
        if(ip_num1 > 255 || ip_num2 > 255 || ip_num3 > 255 || ip_num4 > 255)
            throw new IndexOutOfBoundsException();
        return (ip_num1 << 24) | (ip_num2 << 16) | (ip_num3 << 8) | ip_num4;
    }
    private static int parseUInt(String s) {
        int uint = Integer.parseInt(s);
        if(uint < 0)
            throw new IndexOutOfBoundsException();
        return uint;
    }
}
