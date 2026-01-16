package me.zmaster.zgui.util;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderResolver {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^}]+)}");

    public static String process(String input, Map<String, Object> values) {
        Matcher matcher;

        // 1. Substitui placeholders simples ({nome} -> valor)
        matcher = PLACEHOLDER_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);

            if (values.containsKey(key)) {
                Object val = values.get(key);
                matcher.appendReplacement(sb, Matcher.quoteReplacement(val.toString()));
            } else {
                // Mantém o placeholder para processar como expressão
                matcher.appendReplacement(sb, matcher.group(0));
            }
        }

        matcher.appendTail(sb);
        input = sb.toString();

        // 2. Agora avalia expressões ({2 * valor + 10})
        matcher = PLACEHOLDER_PATTERN.matcher(input);
        sb = new StringBuffer();

        while (matcher.find()) {
            String expressionText = matcher.group(1);

            Expression expression = new ExpressionBuilder(expressionText).build();
            double result = expression.evaluate();

            // Remove ".0" caso seja número inteiro
            String formatted = (result == Math.floor(result))
                    ? String.valueOf((long) result)
                    : String.valueOf(result);

            matcher.appendReplacement(sb, formatted);

        }

        matcher.appendTail(sb);

        return sb.toString();
    }
}
