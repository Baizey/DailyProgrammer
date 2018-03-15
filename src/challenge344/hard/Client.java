package challenge344.hard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    String GET(String url){
        return null;
    }

}

class ParsedUrl {
    private final int port;
    private final String url, init, base, path, args, tag;

    private static final Pattern urlRegex = Pattern.compile("(https://)?((\\.?\\w+)+)(:\\d+)?((/\\w+(\\.\\w+)?)*)(([?&]\\w+=\\w+)*)(#\\w+)?");
    ParsedUrl(String str){
        this.url = str;
        Matcher matcher = urlRegex.matcher(str);
        if(!matcher.find()) throw new IllegalArgumentException("URL is not properly formatted");
        init = matcher.group(1) == null ? "http://" : matcher.group(1);
        base = matcher.group(2);
        port = matcher.group(4) == null ? (init.equals("http://") ? 80 : 443) : Integer.parseInt(matcher.group(4).substring(1));
        path = matcher.group(5) == null ? "" : matcher.group(5);
        args = matcher.group(8) == null ? "" : matcher.group(8);
        tag = matcher.group(10) == null ? "" : matcher.group(10);
    }

    int getPort(){ return port; }

    String getPath(){ return path; }

    String getBaseUrl(){ return init + base; }

    String getArgs(){ return args + tag; }

    String getUrl(){ return url; }
}