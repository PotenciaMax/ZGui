package me.zmaster.zgui.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Formatter {

    // Detecção do exp4j
    private static final boolean SUPPORTS_MATH;
    // Regex para capturar {placeholder}
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^}]+)}");
    // Formatter para evitar 1.9999999 e remover zeros à direita (ex: 2.50 -> 2.5)
    private static final DecimalFormat DECIMAL_FORMAT;

    static {
        boolean mathSupported;
        try {
            Class.forName("net.objecthunter.exp4j.Expression");
            mathSupported = true;
        } catch (ClassNotFoundException e) {
            mathSupported = false;
        }
        SUPPORTS_MATH = mathSupported;

        // Configura para usar PONTO como separador decimal (padrão Minecraft/Java)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        // Formato: Até 2 casas decimais. Se for inteiro, não mostra ponto.
        DECIMAL_FORMAT = new DecimalFormat("0.##", symbols);
    }

    private final Map<String, Object> variables;
    private final Map<String, Double> numericalVariables;

    private Formatter(Map<String, Object> variables, Map<String, Double> numericalVariables) {
        this.variables = variables;
        this.numericalVariables = numericalVariables;
    }

    public static final class Builder {
        private final Map<String, Object> variables = new HashMap<>();

        public Builder add(String key, Object value) {
            variables.put(key, value);
            return this;
        }

        public Formatter build() {
            // Cria o mapa numérico separadamente para otimizar o Math
            Map<String, Double> numerical = new HashMap<>();
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                if (entry.getValue() instanceof Number) {
                    numerical.put(entry.getKey(), ((Number) entry.getValue()).doubleValue());
                }
            }
            return new Formatter(Collections.unmodifiableMap(variables), Collections.unmodifiableMap(numerical));
        }
    }

    // --- Métodos Públicos ---

    public List<String> format(@NotNull List<String> list) {
        Objects.requireNonNull(list, "A lista não pode ser nula");
        if (list.isEmpty()) return list;

        List<String> result = new ArrayList<>(list.size());
        for (String line : list) {
            result.add(format(line));
        }
        return result;
    }

    @Nullable
    public ItemStack format(@Nullable ItemStack item) {
        if (item == null) return null;
        if (!item.hasItemMeta()) return item.clone();

        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();

        // Formata DisplayName
        if (meta.hasDisplayName()) {
            meta.setDisplayName(format(meta.getDisplayName()));
        }

        // Formata Lore
        if (meta.hasLore()) {
            meta.setLore(format(meta.getLore()));
        }

        clone.setItemMeta(meta);
        return clone;
    }

    public String format(String text) {
        if (text == null || text.isEmpty()) return text;

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);

        // StringBuffer é thread-safe e necessário para o appendReplacement antiga
        // StringBuilder é preferível em Java moderno, mas appendReplacement pede StringBuffer até Java 9+
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String content = matcher.group(1).trim();
            String replacement = resolvePlaceholder(content);

            // quoteReplacement previne erro se o texto tiver "$" ou "\"
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    // --- Lógica Interna ---

    private String resolvePlaceholder(String content) {
        // 1. Tenta substituição direta (mais rápido)
        Object directValue = variables.get(content);
        if (directValue != null) {
            if (directValue instanceof Number) {
                return formatDouble(((Number) directValue).doubleValue());
            }
            return directValue.toString();
        }

        // 2. Tenta Math (se ativado e houver variáveis numéricas)
        if (SUPPORTS_MATH && !numericalVariables.isEmpty()) {
            try {
                // Tenta construir a expressão apenas se parecer matemática
                Expression expression = new ExpressionBuilder(content)
                        .variables(numericalVariables.keySet())
                        .build()
                        .setVariables(numericalVariables);

                double result = expression.evaluate();
                return formatDouble(result);

            } catch (Exception ignored) {
                // Se falhar (ex: placeholder de texto que não está no mapa), retorna o original
            }
        }

        // 3. Retorna o placeholder original com as chaves se nada funcionar
        return "{" + content + "}";
    }

    /**
     * Formata double removendo imprecisões de ponto flutuante e zeros desnecessários.
     */
    private String formatDouble(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            return String.valueOf(value);
        }

        // Tolerância para corrigir erros de ponto flutuante (epsilon)
        // Ex: 1.999999999 vira 2.0
        double rounded = Math.round(value);
        if (Math.abs(value - rounded) < 0.000001) {
            return String.valueOf((long) rounded);
        }

        // Se não for "inteiro", usa o formatador (ex: 1.5555 -> 1.56)
        synchronized (DECIMAL_FORMAT) {
            return DECIMAL_FORMAT.format(value);
        }
    }
}
