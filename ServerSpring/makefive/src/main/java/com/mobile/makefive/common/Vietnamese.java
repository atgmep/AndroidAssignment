package com.mobile.makefive.common;

public class Vietnamese {

    private static final char[] AH = {
            'Á', 'À', 'Ả', 'Ã', 'Ạ', 'A',
            'Ắ', 'Ằ', 'Ẩ', 'Ẵ', 'Ặ', 'Ă',
            'Ấ', 'Ầ', 'Ẩ', 'Ẫ', 'Ậ', 'Â'};
    private static final char[] AT = {
            'á', 'à', 'ả', 'ã', 'ạ', 'a',
            'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ', 'ă',
            'ấ', 'ầ', 'ẩ', 'ẫ', 'ậ', 'â'};
    private static final char[] EH = {
            'É', 'È', 'Ẻ', 'Ẽ', 'Ẹ', 'E',
            'Ế', 'Ề', 'Ể', 'Ễ', 'Ệ', 'Ê'};
    private static final char[] ET = {
            'é', 'è', 'ẻ', 'ẽ', 'ẹ', 'e',
            'ế', 'ề', 'ể', 'ễ', 'ệ', 'ê'};
    private static final char[] IH = {
            'Í', 'Ì', 'Ỉ', 'Ĩ', 'Ị', 'I'};
    private static final char[] IT = {
            'í', 'ì', 'ỉ', 'ĩ', 'ị', 'i'};
    private static final char[] OH = {
            'Ó', 'Ò', 'Ỏ', 'Õ', 'Ọ', 'O',
            'Ố', 'Ồ', 'Ỗ', 'Ỗ', 'Ộ', 'Ô',
            'Ớ', 'Ờ', 'Ỡ', 'Ỡ', 'Ợ', 'Ơ'};
    private static final char[] OT = {
            'ó', 'ò', 'ỏ', 'õ', 'ọ', 'o',
            'ố', 'ồ', 'ổ', 'ỗ', 'ộ', 'ô',
            'ớ', 'ờ', 'ở', 'ỡ', 'ợ', 'ơ'};
    private static final char[] UH = {
            'Ú', 'Ù', 'Ủ', 'Ũ', 'Ụ', 'U',
            'Ứ', 'Ừ', 'Ử', 'Ữ', 'Ự', 'Ư'};
    private static final char[] UT = {
            'ú', 'ù', 'ủ', 'ũ', 'ụ', 'u',
            'ứ', 'ừ', 'ử', 'ữ', 'ự', 'ư'};
    private static final char[] YH = {
            'Ý', 'Ỳ', 'Ỷ', 'Ỹ', 'Ỵ', 'Y'};
    private static final char[] YT = {
            'ý', 'ỳ', 'ỷ', 'ỹ', 'ỵ', 'y'};
    private static final char[] DH = {'Đ'};
    private static final char[] DT = {'đ'};

    public static String toLower(String input) {
        String output = input;
        for (int i = 0; i < AH.length; i++) {
            output = output.replace(AH[i], AT[i]);
        }
        for (int i = 0; i < EH.length; i++) {
            output = output.replace(EH[i], ET[i]);
        }
        for (int i = 0; i < IH.length; i++) {
            output = output.replace(IH[i], IT[i]);
        }
        for (int i = 0; i < OH.length; i++) {
            output = output.replace(OH[i], OT[i]);
        }
        for (int i = 0; i < UH.length; i++) {
            output = output.replace(UH[i], UT[i]);
        }
        for (int i = 0; i < YH.length; i++) {
            output = output.replace(YH[i], YT[i]);
        }
        for (int i = 0; i < DH.length; i++) {
            output = output.replace(DH[i], DT[i]);
        }
        output = output.toLowerCase();
        return output;
    }

    public static String toUpperName(String input) {
        String[] parts = input.split(" ");
        StringBuilder output = new StringBuilder();
        for (String part : parts) {
            char[] chars = part.toCharArray();
            for (int i = 0; i < AH.length; i++) {
                if (chars[0] == AT[i]) {
                    chars[0] = AH[i];
                }
            }
            for (int i = 0; i < EH.length; i++) {
                if (chars[0] == ET[i]) {
                    chars[0] = EH[i];
                }
            }
            for (int i = 0; i < IH.length; i++) {
                if (chars[0] == IT[i]) {
                    chars[0] = IH[i];
                }
            }
            for (int i = 0; i < OH.length; i++) {
                if (chars[0] == OT[i]) {
                    chars[0] = OH[i];
                }
            }
            for (int i = 0; i < UH.length; i++) {
                if (chars[0] == UT[i]) {
                    chars[0] = UH[i];
                }
            }
            for (int i = 0; i < YH.length; i++) {
                if (chars[0] == YT[i]) {
                    chars[0] = YH[i];
                }
            }
            for (int i = 0; i < DH.length; i++) {
                if (chars[0] == DT[i]) {
                    chars[0] = DH[i];
                }
            }
            if ('a' <= chars[0] && chars[0] <= 'z') {
                chars[0] = (char) (chars[0] - 32);
            }
            output.append(new String(chars)).append(" ");
        }
        return output.toString().trim();
    }
}
