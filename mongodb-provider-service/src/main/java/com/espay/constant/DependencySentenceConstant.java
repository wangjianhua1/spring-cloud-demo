package com.espay.constant;

import java.util.HashMap;
import java.util.Map;

public class DependencySentenceConstant {
    public static Map<String, Float> DEPREL_WEIGHT = new HashMap<String, Float>() {{
        put("定中关系", 1.0f);
        put("主谓关系", 3.0f);
        put("状中结构", 1.0f);
        put("核心关系", 3.0f);
        put("兼语", 1.0f);
        put("介宾关系", 2.0f);
        put("动宾关系", 2.0f);
        put("并列关系", 1.0f);
        put("右附加关系", 1.0f);
        put("标点符号", 0.3f);
        put("左附加关系", 1.0f);
    }};
}
