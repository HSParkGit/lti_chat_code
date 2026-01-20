/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.lomtech.helloworld.lti.util.IframeWrappingUtil
 */
package net.lomtech.lti.util;

public class IframeWrappingUtil {
    public static String getIframeWrapHTMLStringForLti(String inputURL) {
        return "<!DOCTYPE html>\n<html lang=\"en\">\n   <head>\n       <meta charset=\"UTF-8\">\n       <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n       <title>LTI Launch Successful</title>\n       <style>\n           body { font-family: Arial, sans-serif; margin: 0px;  }\n           h1 { color: #4CAF50; }\n            .info { margin-top: 20px; }\n          .iframe-container {\n              width: 100%;       /* full width of parent */\n              height: 100%;      /* full height of parent */\n              min-height: 100vh;               position: relative; /* for absolute child positioning */\n           }\n\n           .iframe-container iframe {\n               width: 100%;\n               height: 100%;\n               min-height: 100vh; \" +               border: 0;\n             }\n                                        </style>\n   </head>\n   <body>\n       <div class='iframe-container'>           <iframe  src='" + inputURL + "' />\n       </div>\n   </body>\n</html>";
    }
}

