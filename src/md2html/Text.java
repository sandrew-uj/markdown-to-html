package md2html;

import java.util.Map;
import java.util.Stack;

public class Text {         //class for text parsing
    private final String markup;
    private final StringBuilder html;
    private final Stack<String> tags;

    private static final Map<String, String> htmlTags;
    private static final Map<String, String> specials;

    static {
         htmlTags = Map.of(     //tags for html
                "*", "em",
                "_", "em",
                "**", "strong",
                "__", "strong",
                "--", "s",
                "`", "code",
                "```", "pre"
        );

         specials = Map.of(     //special symbols
                ">", "&gt;",
                "<", "&lt;",
                "&", "&amp;",
                "\\*", "*",
                "\\_", "_",
                "\\`", "`"
        );
    }

    public Text(String markup) {
        this.markup = markup;
        html = new StringBuilder();
        tags = new Stack<>();
    }

    private String parseTag(String tag) {       //tag parsing
        String oneTag = tag.substring(0, 1), twoTag = tag.substring(0, 2);
        if (htmlTags.get(tag) != null) {
            return tag;
        } else if (htmlTags.get(twoTag) != null
            || Character.isWhitespace(twoTag.charAt(1))
                && htmlTags.get(oneTag) != null
                && (tags.isEmpty() || !htmlTags.get(oneTag).equals(tags.peek()))
            || oneTag.equals("\\")) {
            return twoTag;
        } else if (htmlTags.get(oneTag) != null || specials.get(oneTag) != null) {
            return oneTag;
        } else {
            return null;
        }
    }

    String toHtml() {
        boolean isConvertible = true;
        for (int i = 0; i < markup.length() - 1; ++i) {
            String tag = parseTag(markup.substring(i, Math.min(i + 3, markup.length())));
            if (tag != null) {
                i += tag.length() - 1;
                if (tag.equals("```")) {
                    isConvertible = true;
                }
                String newTag = htmlTags.get(tag);
                if (newTag != null && isConvertible) {
                    if (!tags.isEmpty() && newTag.equals(tags.peek())) {
                        html.append("</").append(newTag).append(">");
                        tags.pop();
                    } else {
                        if (tag.equals("```")) {
                            isConvertible = false;
                        }
                        html.append("<").append(newTag).append(">");
                        tags.push(newTag);
                    }
                } else {
                    html.append(specials.getOrDefault(tag, tag));
                }
            } else {
                html.append(markup.charAt(i));
            }
        }
        return html.toString();
    }
}