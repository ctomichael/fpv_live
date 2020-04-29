package com.mapbox.mapboxsdk.style.expressions;

import android.annotation.SuppressLint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import dji.component.accountcenter.IMemberProtocol;
import dji.component.mediaprovider.DJIMediaStore;
import dji.publics.LogReport.base.Fields;
import dji.publics.protocol.ResponseBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Expression {
    @Nullable
    private final Expression[] arguments;
    @Nullable
    private final String operator;

    public static class Array {
    }

    private interface ValueExpression {
        Object toValue();
    }

    Expression() {
        this.operator = null;
        this.arguments = null;
    }

    public Expression(@NonNull String operator2, @Nullable Expression... arguments2) {
        this.operator = operator2;
        this.arguments = arguments2;
    }

    public static Expression literal(@NonNull Number number) {
        return new ExpressionLiteral(number);
    }

    public static Expression literal(@NonNull String string) {
        return new ExpressionLiteral(string);
    }

    public static Expression literal(boolean bool) {
        return new ExpressionLiteral(Boolean.valueOf(bool));
    }

    public static Expression literal(@NonNull Object object) {
        if (object.getClass().isArray()) {
            return literal(toObjectArray(object));
        }
        if (!(object instanceof Expression)) {
            return new ExpressionLiteral(object);
        }
        throw new RuntimeException("Can't convert an expression to a literal");
    }

    public static Expression literal(@NonNull Object[] array) {
        return new Expression("literal", new ExpressionLiteralArray(array));
    }

    public static Expression color(@ColorInt int color) {
        float[] rgba = ColorUtils.colorToRgbaArray(color);
        return rgba(Float.valueOf(rgba[0]), Float.valueOf(rgba[1]), Float.valueOf(rgba[2]), Float.valueOf(rgba[3]));
    }

    public static Expression rgb(@NonNull Expression red, @NonNull Expression green, @NonNull Expression blue) {
        return new Expression("rgb", red, green, blue);
    }

    public static Expression rgb(@NonNull Number red, @NonNull Number green, @NonNull Number blue) {
        return rgb(literal(red), literal(green), literal(blue));
    }

    public static Expression rgba(@NonNull Expression red, @NonNull Expression green, @NonNull Expression blue, @NonNull Expression alpha) {
        return new Expression("rgba", red, green, blue, alpha);
    }

    public static Expression rgba(@NonNull Number red, @NonNull Number green, @NonNull Number blue, @NonNull Number alpha) {
        return rgba(literal(red), literal(green), literal(blue), literal(alpha));
    }

    public static Expression toRgba(@NonNull Expression expression) {
        return new Expression("to-rgba", expression);
    }

    public static Expression eq(@NonNull Expression compareOne, @NonNull Expression compareTwo) {
        return new Expression("==", compareOne, compareTwo);
    }

    public static Expression eq(@NonNull Expression compareOne, @NonNull Expression compareTwo, @NonNull Expression collator) {
        return new Expression("==", compareOne, compareTwo, collator);
    }

    public static Expression eq(@NonNull Expression compareOne, boolean compareTwo) {
        return eq(compareOne, literal(compareTwo));
    }

    public static Expression eq(@NonNull Expression compareOne, @NonNull String compareTwo) {
        return eq(compareOne, literal(compareTwo));
    }

    public static Expression eq(@NonNull Expression compareOne, @NonNull String compareTwo, @NonNull Expression collator) {
        return eq(compareOne, literal(compareTwo), collator);
    }

    public static Expression eq(@NonNull Expression compareOne, @NonNull Number compareTwo) {
        return eq(compareOne, literal(compareTwo));
    }

    public static Expression neq(@NonNull Expression compareOne, @NonNull Expression compareTwo) {
        return new Expression("!=", compareOne, compareTwo);
    }

    public static Expression neq(@NonNull Expression compareOne, @NonNull Expression compareTwo, @NonNull Expression collator) {
        return new Expression("!=", compareOne, compareTwo, collator);
    }

    public static Expression neq(Expression compareOne, boolean compareTwo) {
        return new Expression("!=", compareOne, literal(compareTwo));
    }

    public static Expression neq(@NonNull Expression compareOne, @NonNull String compareTwo) {
        return new Expression("!=", compareOne, literal(compareTwo));
    }

    public static Expression neq(@NonNull Expression compareOne, @NonNull String compareTwo, @NonNull Expression collator) {
        return new Expression("!=", compareOne, literal(compareTwo), collator);
    }

    public static Expression neq(@NonNull Expression compareOne, @NonNull Number compareTwo) {
        return new Expression("!=", compareOne, literal(compareTwo));
    }

    public static Expression gt(@NonNull Expression compareOne, @NonNull Expression compareTwo) {
        return new Expression(">", compareOne, compareTwo);
    }

    public static Expression gt(@NonNull Expression compareOne, @NonNull Expression compareTwo, @NonNull Expression collator) {
        return new Expression(">", compareOne, compareTwo, collator);
    }

    public static Expression gt(@NonNull Expression compareOne, @NonNull Number compareTwo) {
        return new Expression(">", compareOne, literal(compareTwo));
    }

    public static Expression gt(@NonNull Expression compareOne, @NonNull String compareTwo) {
        return new Expression(">", compareOne, literal(compareTwo));
    }

    public static Expression gt(@NonNull Expression compareOne, @NonNull String compareTwo, @NonNull Expression collator) {
        return new Expression(">", compareOne, literal(compareTwo), collator);
    }

    public static Expression lt(@NonNull Expression compareOne, @NonNull Expression compareTwo) {
        return new Expression("<", compareOne, compareTwo);
    }

    public static Expression lt(@NonNull Expression compareOne, @NonNull Expression compareTwo, @NonNull Expression collator) {
        return new Expression("<", compareOne, compareTwo, collator);
    }

    public static Expression lt(@NonNull Expression compareOne, @NonNull Number compareTwo) {
        return new Expression("<", compareOne, literal(compareTwo));
    }

    public static Expression lt(@NonNull Expression compareOne, @NonNull String compareTwo) {
        return new Expression("<", compareOne, literal(compareTwo));
    }

    public static Expression lt(@NonNull Expression compareOne, @NonNull String compareTwo, @NonNull Expression collator) {
        return new Expression("<", compareOne, literal(compareTwo), collator);
    }

    public static Expression gte(@NonNull Expression compareOne, @NonNull Expression compareTwo) {
        return new Expression(">=", compareOne, compareTwo);
    }

    public static Expression gte(@NonNull Expression compareOne, @NonNull Expression compareTwo, @NonNull Expression collator) {
        return new Expression(">=", compareOne, compareTwo, collator);
    }

    public static Expression gte(@NonNull Expression compareOne, @NonNull Number compareTwo) {
        return new Expression(">=", compareOne, literal(compareTwo));
    }

    public static Expression gte(@NonNull Expression compareOne, @NonNull String compareTwo) {
        return new Expression(">=", compareOne, literal(compareTwo));
    }

    public static Expression gte(@NonNull Expression compareOne, @NonNull String compareTwo, @NonNull Expression collator) {
        return new Expression(">=", compareOne, literal(compareTwo), collator);
    }

    public static Expression lte(@NonNull Expression compareOne, @NonNull Expression compareTwo) {
        return new Expression("<=", compareOne, compareTwo);
    }

    public static Expression lte(@NonNull Expression compareOne, @NonNull Expression compareTwo, @NonNull Expression collator) {
        return new Expression("<=", compareOne, compareTwo, collator);
    }

    public static Expression lte(@NonNull Expression compareOne, @NonNull Number compareTwo) {
        return new Expression("<=", compareOne, literal(compareTwo));
    }

    public static Expression lte(@NonNull Expression compareOne, @NonNull String compareTwo) {
        return new Expression("<=", compareOne, literal(compareTwo));
    }

    public static Expression lte(@NonNull Expression compareOne, @NonNull String compareTwo, @NonNull Expression collator) {
        return new Expression("<=", compareOne, literal(compareTwo), collator);
    }

    public static Expression all(@NonNull Expression... input) {
        return new Expression("all", input);
    }

    public static Expression any(@NonNull Expression... input) {
        return new Expression("any", input);
    }

    public static Expression not(@NonNull Expression input) {
        return new Expression("!", input);
    }

    public static Expression not(boolean input) {
        return not(literal(input));
    }

    public static Expression switchCase(@Size(min = 1) @NonNull Expression... input) {
        return new Expression("case", input);
    }

    public static Expression match(@Size(min = 2) @NonNull Expression... input) {
        return new Expression("match", input);
    }

    public static Expression match(@NonNull Expression input, @NonNull Expression defaultOutput, @NonNull Stop... stops) {
        return match(join(join(new Expression[]{input}, Stop.toExpressionArray(stops)), new Expression[]{defaultOutput}));
    }

    public static Expression coalesce(@NonNull Expression... input) {
        return new Expression("coalesce", input);
    }

    public static Expression properties() {
        return new Expression("properties", new Expression[0]);
    }

    public static Expression geometryType() {
        return new Expression("geometry-type", new Expression[0]);
    }

    public static Expression id() {
        return new Expression(ResponseBase.STRING_ID, new Expression[0]);
    }

    public static Expression accumulated() {
        return new Expression("accumulated", new Expression[0]);
    }

    public static Expression heatmapDensity() {
        return new Expression("heatmap-density", new Expression[0]);
    }

    public static Expression lineProgress() {
        return new Expression("line-progress", new Expression[0]);
    }

    public static Expression at(@NonNull Expression number, @NonNull Expression expression) {
        return new Expression("at", number, expression);
    }

    public static Expression at(@NonNull Number number, @NonNull Expression expression) {
        return at(literal(number), expression);
    }

    public static Expression get(@NonNull Expression input) {
        return new Expression("get", input);
    }

    public static Expression get(@NonNull String input) {
        return get(literal(input));
    }

    public static Expression get(@NonNull Expression key, @NonNull Expression object) {
        return new Expression("get", key, object);
    }

    public static Expression get(@NonNull String key, @NonNull Expression object) {
        return get(literal(key), object);
    }

    public static Expression has(@NonNull Expression key) {
        return new Expression("has", key);
    }

    public static Expression has(@NonNull String key) {
        return has(literal(key));
    }

    public static Expression has(@NonNull Expression key, @NonNull Expression object) {
        return new Expression("has", key, object);
    }

    public static Expression has(@NonNull String key, @NonNull Expression object) {
        return has(literal(key), object);
    }

    public static Expression length(@NonNull Expression expression) {
        return new Expression(DJIMediaStore.FileColumns.LENGTH, expression);
    }

    public static Expression length(@NonNull String input) {
        return length(literal(input));
    }

    public static Expression ln2() {
        return new Expression("ln2", new Expression[0]);
    }

    public static Expression pi() {
        return new Expression("pi", new Expression[0]);
    }

    public static Expression e() {
        return new Expression("e", new Expression[0]);
    }

    public static Expression sum(@Size(min = 2) Expression... numbers) {
        return new Expression("+", numbers);
    }

    @SuppressLint({"Range"})
    public static Expression sum(@Size(min = 2) Number... numbers) {
        Expression[] numberExpression = new Expression[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            numberExpression[i] = literal(numbers[i]);
        }
        return sum(numberExpression);
    }

    public static Expression product(@Size(min = 2) Expression... numbers) {
        return new Expression("*", numbers);
    }

    @SuppressLint({"Range"})
    public static Expression product(@Size(min = 2) Number... numbers) {
        Expression[] numberExpression = new Expression[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            numberExpression[i] = literal(numbers[i]);
        }
        return product(numberExpression);
    }

    public static Expression subtract(@NonNull Expression number) {
        return new Expression("-", number);
    }

    public static Expression subtract(@NonNull Number number) {
        return subtract(literal(number));
    }

    public static Expression subtract(@NonNull Expression first, @NonNull Expression second) {
        return new Expression("-", first, second);
    }

    public static Expression subtract(@NonNull Number first, @NonNull Number second) {
        return subtract(literal(first), literal(second));
    }

    public static Expression division(@NonNull Expression first, @NonNull Expression second) {
        return new Expression(IMemberProtocol.PARAM_SEPERATOR, first, second);
    }

    public static Expression division(@NonNull Number first, @NonNull Number second) {
        return division(literal(first), literal(second));
    }

    public static Expression mod(@NonNull Expression first, @NonNull Expression second) {
        return new Expression("%", first, second);
    }

    public static Expression mod(@NonNull Number first, @NonNull Number second) {
        return mod(literal(first), literal(second));
    }

    public static Expression pow(@NonNull Expression first, @NonNull Expression second) {
        return new Expression("^", first, second);
    }

    public static Expression pow(@NonNull Number first, @NonNull Number second) {
        return pow(literal(first), literal(second));
    }

    public static Expression sqrt(@NonNull Expression number) {
        return new Expression("sqrt", number);
    }

    public static Expression sqrt(@NonNull Number number) {
        return sqrt(literal(number));
    }

    public static Expression log10(@NonNull Expression number) {
        return new Expression("log10", number);
    }

    public static Expression log10(@NonNull Number number) {
        return log10(literal(number));
    }

    public static Expression ln(Expression number) {
        return new Expression("ln", number);
    }

    public static Expression ln(@NonNull Number number) {
        return ln(literal(number));
    }

    public static Expression log2(@NonNull Expression number) {
        return new Expression("log2", number);
    }

    public static Expression log2(@NonNull Number number) {
        return log2(literal(number));
    }

    public static Expression sin(@NonNull Expression number) {
        return new Expression("sin", number);
    }

    public static Expression sin(@NonNull Number number) {
        return sin(literal(number));
    }

    public static Expression cos(@NonNull Expression number) {
        return new Expression("cos", number);
    }

    public static Expression cos(@NonNull Number number) {
        return new Expression("cos", literal(number));
    }

    public static Expression tan(@NonNull Expression number) {
        return new Expression("tan", number);
    }

    public static Expression tan(@NonNull Number number) {
        return new Expression("tan", literal(number));
    }

    public static Expression asin(@NonNull Expression number) {
        return new Expression("asin", number);
    }

    public static Expression asin(@NonNull Number number) {
        return asin(literal(number));
    }

    public static Expression acos(@NonNull Expression number) {
        return new Expression("acos", number);
    }

    public static Expression acos(@NonNull Number number) {
        return acos(literal(number));
    }

    public static Expression atan(@NonNull Expression number) {
        return new Expression("atan", number);
    }

    public static Expression atan(@NonNull Number number) {
        return atan(literal(number));
    }

    public static Expression min(@Size(min = 1) Expression... numbers) {
        return new Expression("min", numbers);
    }

    @SuppressLint({"Range"})
    public static Expression min(@Size(min = 1) Number... numbers) {
        Expression[] numberExpression = new Expression[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            numberExpression[i] = literal(numbers[i]);
        }
        return min(numberExpression);
    }

    public static Expression max(@Size(min = 1) Expression... numbers) {
        return new Expression("max", numbers);
    }

    @SuppressLint({"Range"})
    public static Expression max(@Size(min = 1) Number... numbers) {
        Expression[] numberExpression = new Expression[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            numberExpression[i] = literal(numbers[i]);
        }
        return max(numberExpression);
    }

    public static Expression round(Expression expression) {
        return new Expression("round", expression);
    }

    public static Expression round(@NonNull Number number) {
        return round(literal(number));
    }

    public static Expression abs(Expression expression) {
        return new Expression("abs", expression);
    }

    public static Expression abs(@NonNull Number number) {
        return abs(literal(number));
    }

    public static Expression ceil(Expression expression) {
        return new Expression("ceil", expression);
    }

    public static Expression ceil(@NonNull Number number) {
        return ceil(literal(number));
    }

    public static Expression floor(Expression expression) {
        return new Expression("floor", expression);
    }

    public static Expression floor(@NonNull Number number) {
        return floor(literal(number));
    }

    public static Expression resolvedLocale(Expression collator) {
        return new Expression("resolved-locale", collator);
    }

    public static Expression isSupportedScript(Expression expression) {
        return new Expression("is-supported-script", expression);
    }

    public static Expression isSupportedScript(@NonNull String string) {
        return new Expression("is-supported-script", literal(string));
    }

    public static Expression upcase(@NonNull Expression string) {
        return new Expression("upcase", string);
    }

    public static Expression upcase(@NonNull String string) {
        return upcase(literal(string));
    }

    public static Expression downcase(@NonNull Expression input) {
        return new Expression("downcase", input);
    }

    public static Expression downcase(@NonNull String input) {
        return downcase(literal(input));
    }

    public static Expression concat(@NonNull Expression... input) {
        return new Expression("concat", input);
    }

    public static Expression concat(@NonNull String... input) {
        Expression[] stringExpression = new Expression[input.length];
        for (int i = 0; i < input.length; i++) {
            stringExpression[i] = literal(input[i]);
        }
        return concat(stringExpression);
    }

    public static Expression array(@NonNull Expression input) {
        return new Expression("array", input);
    }

    public static Expression typeOf(@NonNull Expression input) {
        return new Expression("typeof", input);
    }

    public static Expression string(@NonNull Expression... input) {
        return new Expression("string", input);
    }

    public static Expression number(@NonNull Expression... input) {
        return new Expression("number", input);
    }

    public static Expression numberFormat(@NonNull Expression number, @NonNull NumberFormatOption... options) {
        Map<String, Expression> map = new HashMap<>();
        for (NumberFormatOption option : options) {
            map.put(option.type, option.value);
        }
        return new Expression("number-format", number, new ExpressionMap(map));
    }

    public static Expression numberFormat(@NonNull Number number, @NonNull NumberFormatOption... options) {
        return numberFormat(literal(number), options);
    }

    public static Expression bool(@NonNull Expression... input) {
        return new Expression("boolean", input);
    }

    public static Expression collator(boolean caseSensitive, boolean diacriticSensitive, Locale locale) {
        Map<String, Expression> map = new HashMap<>();
        map.put("case-sensitive", literal(caseSensitive));
        map.put("diacritic-sensitive", literal(diacriticSensitive));
        StringBuilder localeStringBuilder = new StringBuilder();
        String language = locale.getLanguage();
        if (language != null && !language.isEmpty()) {
            localeStringBuilder.append(language);
        }
        String country = locale.getCountry();
        if (country != null && !country.isEmpty()) {
            localeStringBuilder.append("-");
            localeStringBuilder.append(country);
        }
        map.put("locale", literal(localeStringBuilder.toString()));
        return new Expression("collator", new ExpressionMap(map));
    }

    public static Expression collator(boolean caseSensitive, boolean diacriticSensitive) {
        Map<String, Expression> map = new HashMap<>();
        map.put("case-sensitive", literal(caseSensitive));
        map.put("diacritic-sensitive", literal(diacriticSensitive));
        return new Expression("collator", new ExpressionMap(map));
    }

    public static Expression collator(Expression caseSensitive, Expression diacriticSensitive, Expression locale) {
        Map<String, Expression> map = new HashMap<>();
        map.put("case-sensitive", caseSensitive);
        map.put("diacritic-sensitive", diacriticSensitive);
        map.put("locale", locale);
        return new Expression("collator", new ExpressionMap(map));
    }

    public static Expression collator(Expression caseSensitive, Expression diacriticSensitive) {
        Map<String, Expression> map = new HashMap<>();
        map.put("case-sensitive", caseSensitive);
        map.put("diacritic-sensitive", diacriticSensitive);
        return new Expression("collator", new ExpressionMap(map));
    }

    public static Expression format(@NonNull FormatEntry... formatEntries) {
        Expression[] mappedExpressions = new Expression[(formatEntries.length * 2)];
        int mappedIndex = 0;
        for (FormatEntry formatEntry : formatEntries) {
            int mappedIndex2 = mappedIndex + 1;
            mappedExpressions[mappedIndex] = formatEntry.text;
            Map<String, Expression> map = new HashMap<>();
            if (formatEntry.options != null) {
                FormatOption[] access$100 = formatEntry.options;
                for (FormatOption option : access$100) {
                    map.put(option.type, option.value);
                }
            }
            mappedIndex = mappedIndex2 + 1;
            mappedExpressions[mappedIndex2] = new ExpressionMap(map);
        }
        return new Expression(Fields.Dgo_takephoto.EVENT_FORMT, mappedExpressions);
    }

    public static FormatEntry formatEntry(@NonNull Expression text, @Nullable FormatOption... formatOptions) {
        return new FormatEntry(text, formatOptions);
    }

    public static FormatEntry formatEntry(@NonNull Expression text) {
        return new FormatEntry(text, null);
    }

    public static FormatEntry formatEntry(@NonNull String text, @Nullable FormatOption... formatOptions) {
        return new FormatEntry(literal(text), formatOptions);
    }

    public static FormatEntry formatEntry(@NonNull String text) {
        return new FormatEntry(literal(text), null);
    }

    public static Expression object(@NonNull Expression input) {
        return new Expression("object", input);
    }

    public static Expression toString(@NonNull Expression input) {
        return new Expression("to-string", input);
    }

    public static Expression toNumber(@NonNull Expression input) {
        return new Expression("to-number", input);
    }

    public static Expression toBool(@NonNull Expression input) {
        return new Expression("to-boolean", input);
    }

    public static Expression toColor(@NonNull Expression input) {
        return new Expression("to-color", input);
    }

    public static Expression let(@Size(min = 1) Expression... input) {
        return new Expression("let", input);
    }

    public static Expression var(@NonNull Expression expression) {
        return new Expression("var", expression);
    }

    public static Expression var(@NonNull String variableName) {
        return var(literal(variableName));
    }

    public static Expression zoom() {
        return new Expression("zoom", new Expression[0]);
    }

    public static Stop stop(@NonNull Object stop, @NonNull Object value) {
        return new Stop(stop, value);
    }

    public static Expression step(@NonNull Number input, @NonNull Expression defaultOutput, Expression... stops) {
        return step(literal(input), defaultOutput, stops);
    }

    public static Expression step(@NonNull Expression input, @NonNull Expression defaultOutput, @NonNull Expression... stops) {
        return new Expression(Fields.Dgo_update.STEP, join(new Expression[]{input, defaultOutput}, stops));
    }

    public static Expression step(@NonNull Number input, @NonNull Expression defaultOutput, Stop... stops) {
        return step(literal(input), defaultOutput, Stop.toExpressionArray(stops));
    }

    public static Expression step(@NonNull Expression input, @NonNull Expression defaultOutput, Stop... stops) {
        return step(input, defaultOutput, Stop.toExpressionArray(stops));
    }

    public static Expression step(@NonNull Number input, @NonNull Number defaultOutput, Expression... stops) {
        return step(literal(input), defaultOutput, stops);
    }

    public static Expression step(@NonNull Expression input, @NonNull Number defaultOutput, Expression... stops) {
        return step(input, literal(defaultOutput), stops);
    }

    public static Expression step(@NonNull Number input, @NonNull Number defaultOutput, Stop... stops) {
        return step(literal(input), defaultOutput, Stop.toExpressionArray(stops));
    }

    public static Expression step(@NonNull Expression input, @NonNull Number defaultOutput, Stop... stops) {
        return step(input, defaultOutput, Stop.toExpressionArray(stops));
    }

    public static Expression interpolate(@NonNull Interpolator interpolation, @NonNull Expression number, @NonNull Expression... stops) {
        return new Expression("interpolate", join(new Expression[]{interpolation, number}, stops));
    }

    public static Expression interpolate(@NonNull Interpolator interpolation, @NonNull Expression number, Stop... stops) {
        return interpolate(interpolation, number, Stop.toExpressionArray(stops));
    }

    public static Interpolator linear() {
        return new Interpolator(Property.RASTER_RESAMPLING_LINEAR, new Expression[0]);
    }

    public static Interpolator exponential(@NonNull Number base) {
        return exponential(literal(base));
    }

    public static Interpolator exponential(@NonNull Expression expression) {
        return new Interpolator("exponential", expression);
    }

    public static Interpolator cubicBezier(@NonNull Expression x1, @NonNull Expression y1, @NonNull Expression x2, @NonNull Expression y2) {
        return new Interpolator("cubic-bezier", x1, y1, x2, y2);
    }

    public static Interpolator cubicBezier(@NonNull Number x1, @NonNull Number y1, @NonNull Number x2, @NonNull Number y2) {
        return cubicBezier(literal(x1), literal(y1), literal(x2), literal(y2));
    }

    @NonNull
    private static Expression[] join(Expression[] left, Expression[] right) {
        Expression[] output = new Expression[(left.length + right.length)];
        System.arraycopy(left, 0, output, 0, left.length);
        System.arraycopy(right, 0, output, left.length, right.length);
        return output;
    }

    @NonNull
    public Object[] toArray() {
        List<Object> array = new ArrayList<>();
        array.add(this.operator);
        if (this.arguments != null) {
            Expression[] expressionArr = this.arguments;
            for (Expression argument : expressionArr) {
                if (argument instanceof ValueExpression) {
                    array.add(((ValueExpression) argument).toValue());
                } else {
                    array.add(argument.toArray());
                }
            }
        }
        return array.toArray();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[\"").append(this.operator).append("\"");
        if (this.arguments != null) {
            Expression[] expressionArr = this.arguments;
            for (Expression argument : expressionArr) {
                builder.append(", ");
                builder.append(argument.toString());
            }
        }
        builder.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return builder.toString();
    }

    public static Expression raw(@NonNull String rawExpression) {
        return Converter.convert(rawExpression);
    }

    public boolean equals(@Nullable Object o) {
        super.equals(o);
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Expression)) {
            return false;
        }
        Expression that = (Expression) o;
        if (this.operator != null) {
            if (!this.operator.equals(that.operator)) {
                return false;
            }
        } else if (that.operator != null) {
            return false;
        }
        return Arrays.deepEquals(this.arguments, that.arguments);
    }

    public int hashCode() {
        return ((this.operator != null ? this.operator.hashCode() : 0) * 31) + Arrays.hashCode(this.arguments);
    }

    public static class ExpressionLiteral extends Expression implements ValueExpression {
        protected Object literal;

        public ExpressionLiteral(@NonNull Object object) {
            if (object instanceof String) {
                object = unwrapStringLiteral((String) object);
            } else if (object instanceof Number) {
                object = Float.valueOf(((Number) object).floatValue());
            }
            this.literal = object;
        }

        public Object toValue() {
            if (this.literal instanceof PropertyValue) {
                throw new IllegalArgumentException("PropertyValue are not allowed as an expression literal, use value instead.");
            } else if (this.literal instanceof ExpressionLiteral) {
                return ((ExpressionLiteral) this.literal).toValue();
            } else {
                return this.literal;
            }
        }

        @NonNull
        public Object[] toArray() {
            return new Object[]{"literal", this.literal};
        }

        public String toString() {
            if (this.literal instanceof String) {
                return "\"" + this.literal + "\"";
            }
            return this.literal.toString();
        }

        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (!Expression.super.equals(o)) {
                return false;
            }
            ExpressionLiteral that = (ExpressionLiteral) o;
            if (this.literal != null) {
                return this.literal.equals(that.literal);
            }
            if (that.literal != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (Expression.super.hashCode() * 31) + (this.literal != null ? this.literal.hashCode() : 0);
        }

        @NonNull
        private static String unwrapStringLiteral(String value) {
            if (value.length() > 1 && value.charAt(0) == '\"' && value.charAt(value.length() - 1) == '\"') {
                return value.substring(1, value.length() - 1);
            }
            return value;
        }
    }

    public static class Interpolator extends Expression {
        Interpolator(@NonNull String operator, @Nullable Expression... arguments) {
            super(operator, arguments);
        }
    }

    public static class Stop {
        private Object output;
        private Object value;

        Stop(Object value2, Object output2) {
            this.value = value2;
            this.output = output2;
        }

        @NonNull
        static Expression[] toExpressionArray(Stop... stops) {
            Expression[] expressions = new Expression[(stops.length * 2)];
            for (int i = 0; i < stops.length; i++) {
                Stop stop = stops[i];
                Object inputValue = stop.value;
                Object outputValue = stop.output;
                if (!(inputValue instanceof Expression)) {
                    inputValue = Expression.literal(inputValue);
                }
                if (!(outputValue instanceof Expression)) {
                    outputValue = Expression.literal(outputValue);
                }
                expressions[i * 2] = (Expression) inputValue;
                expressions[(i * 2) + 1] = (Expression) outputValue;
            }
            return expressions;
        }
    }

    public static class FormatEntry {
        /* access modifiers changed from: private */
        @Nullable
        public FormatOption[] options;
        /* access modifiers changed from: private */
        @NonNull
        public Expression text;

        FormatEntry(@NonNull Expression text2, @Nullable FormatOption[] options2) {
            this.text = text2;
            this.options = options2;
        }
    }

    private static class Option {
        @NonNull
        String type;
        @NonNull
        Expression value;

        Option(@NonNull String type2, @NonNull Expression value2) {
            this.type = type2;
            this.value = value2;
        }
    }

    public static class NumberFormatOption extends Option {
        NumberFormatOption(@NonNull String type, @NonNull Expression value) {
            super(type, value);
        }

        @NonNull
        public static NumberFormatOption locale(@NonNull Expression string) {
            return new NumberFormatOption("locale", string);
        }

        @NonNull
        public static NumberFormatOption locale(@NonNull String string) {
            return new NumberFormatOption("locale", Expression.literal(string));
        }

        @NonNull
        public static NumberFormatOption currency(@NonNull Expression string) {
            return new NumberFormatOption(ResponseBase.STRING_DDS_CURRENCY, string);
        }

        @NonNull
        public static NumberFormatOption currency(@NonNull String string) {
            return new NumberFormatOption(ResponseBase.STRING_DDS_CURRENCY, Expression.literal(string));
        }

        @NonNull
        public static NumberFormatOption minFractionDigits(@NonNull Expression number) {
            return new NumberFormatOption("min-fraction-digits", number);
        }

        @NonNull
        public static NumberFormatOption minFractionDigits(int number) {
            return new NumberFormatOption("min-fraction-digits", Expression.literal((Number) Integer.valueOf(number)));
        }

        @NonNull
        public static NumberFormatOption maxFractionDigits(@NonNull Expression number) {
            return new NumberFormatOption("max-fraction-digits", number);
        }

        @NonNull
        public static NumberFormatOption maxFractionDigits(@NonNull int number) {
            return new NumberFormatOption("max-fraction-digits", Expression.literal((Number) Integer.valueOf(number)));
        }
    }

    public static class FormatOption extends Option {
        FormatOption(@NonNull String type, @NonNull Expression value) {
            super(type, value);
        }

        @NonNull
        public static FormatOption formatFontScale(@NonNull Expression expression) {
            return new FormatOption("font-scale", expression);
        }

        @NonNull
        public static FormatOption formatFontScale(double scale) {
            return new FormatOption("font-scale", Expression.literal((Number) Double.valueOf(scale)));
        }

        @NonNull
        public static FormatOption formatTextFont(@NonNull Expression expression) {
            return new FormatOption("text-font", expression);
        }

        @NonNull
        public static FormatOption formatTextFont(@NonNull String[] fontStack) {
            return new FormatOption("text-font", Expression.literal((Object[]) fontStack));
        }

        @NonNull
        public static FormatOption formatTextColor(@NonNull Expression expression) {
            return new FormatOption("text-color", expression);
        }

        @NonNull
        public static FormatOption formatTextColor(@ColorInt int color) {
            return new FormatOption("text-color", Expression.color(color));
        }
    }

    public static final class Converter {
        private static final Gson gson = new Gson();

        public static Expression convert(@NonNull JsonArray jsonArray) {
            if (jsonArray.size() == 0) {
                throw new IllegalArgumentException("Can't convert empty jsonArray expressions");
            }
            String operator = jsonArray.get(0).getAsString();
            List<Expression> arguments = new ArrayList<>();
            for (int i = 1; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                if (!operator.equals("literal") || !(jsonElement instanceof JsonArray)) {
                    arguments.add(convert(jsonElement));
                } else {
                    JsonArray nestedArray = (JsonArray) jsonElement;
                    Object[] array = new Object[nestedArray.size()];
                    int j = 0;
                    while (j < nestedArray.size()) {
                        JsonElement element = nestedArray.get(j);
                        if (element instanceof JsonPrimitive) {
                            array[j] = convertToValue((JsonPrimitive) element);
                            j++;
                        } else {
                            throw new IllegalArgumentException("Nested literal arrays are not supported.");
                        }
                    }
                    arguments.add(new ExpressionLiteralArray(array));
                }
            }
            return new Expression(operator, (Expression[]) arguments.toArray(new Expression[arguments.size()]));
        }

        public static Expression convert(@NonNull JsonElement jsonElement) {
            if (jsonElement instanceof JsonArray) {
                return convert((JsonArray) jsonElement);
            }
            if (jsonElement instanceof JsonPrimitive) {
                return convert((JsonPrimitive) jsonElement);
            }
            if (jsonElement instanceof JsonNull) {
                return new ExpressionLiteral("");
            }
            if (jsonElement instanceof JsonObject) {
                Map<String, Expression> map = new HashMap<>();
                for (String key : ((JsonObject) jsonElement).keySet()) {
                    map.put(key, convert(((JsonObject) jsonElement).get(key)));
                }
                return new ExpressionMap(map);
            }
            throw new RuntimeException("Unsupported expression conversion for " + jsonElement.getClass());
        }

        private static Expression convert(@NonNull JsonPrimitive jsonPrimitive) {
            return new ExpressionLiteral(convertToValue(jsonPrimitive));
        }

        private static Object convertToValue(@NonNull JsonPrimitive jsonPrimitive) {
            if (jsonPrimitive.isBoolean()) {
                return Boolean.valueOf(jsonPrimitive.getAsBoolean());
            }
            if (jsonPrimitive.isNumber()) {
                return Float.valueOf(jsonPrimitive.getAsFloat());
            }
            if (jsonPrimitive.isString()) {
                return jsonPrimitive.getAsString();
            }
            throw new RuntimeException("Unsupported literal expression conversion for " + jsonPrimitive.getClass());
        }

        public static Expression convert(@NonNull String rawExpression) {
            return convert((JsonArray) gson.fromJson(rawExpression, JsonArray.class));
        }
    }

    private static class ExpressionLiteralArray extends ExpressionLiteral {
        ExpressionLiteralArray(@NonNull Object[] object) {
            super(object);
        }

        @NonNull
        public String toString() {
            Object[] array = (Object[]) this.literal;
            StringBuilder builder = new StringBuilder(IMemberProtocol.STRING_SEPERATOR_LEFT);
            for (int i = 0; i < array.length; i++) {
                Object argument = array[i];
                if (argument instanceof String) {
                    builder.append("\"").append(argument).append("\"");
                } else {
                    builder.append(argument);
                }
                if (i != array.length - 1) {
                    builder.append(", ");
                }
            }
            builder.append(IMemberProtocol.STRING_SEPERATOR_RIGHT);
            return builder.toString();
        }

        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return Arrays.equals((Object[]) this.literal, (Object[]) ((ExpressionLiteralArray) o).literal);
        }
    }

    private static class ExpressionMap extends Expression implements ValueExpression {
        private Map<String, Expression> map;

        ExpressionMap(Map<String, Expression> map2) {
            this.map = map2;
        }

        @NonNull
        public Object toValue() {
            Map<String, Object> unwrappedMap = new HashMap<>();
            for (String key : this.map.keySet()) {
                Expression expression = this.map.get(key);
                if (expression instanceof ValueExpression) {
                    unwrappedMap.put(key, ((ValueExpression) expression).toValue());
                } else {
                    unwrappedMap.put(key, expression.toArray());
                }
            }
            return unwrappedMap;
        }

        @NonNull
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            for (String key : this.map.keySet()) {
                builder.append("\"").append(key).append("\": ");
                builder.append(this.map.get(key));
                builder.append(", ");
            }
            if (this.map.size() > 0) {
                builder.delete(builder.length() - 2, builder.length());
            }
            builder.append("}");
            return builder.toString();
        }

        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass() || !Expression.super.equals(o)) {
                return false;
            }
            return this.map.equals(((ExpressionMap) o).map);
        }

        public int hashCode() {
            return (Expression.super.hashCode() * 31) + (this.map == null ? 0 : this.map.hashCode());
        }
    }

    @NonNull
    private static Object[] toObjectArray(Object object) {
        int len = java.lang.reflect.Array.getLength(object);
        Object[] objects = new Object[len];
        for (int i = 0; i < len; i++) {
            objects[i] = java.lang.reflect.Array.get(object, i);
        }
        return objects;
    }
}
