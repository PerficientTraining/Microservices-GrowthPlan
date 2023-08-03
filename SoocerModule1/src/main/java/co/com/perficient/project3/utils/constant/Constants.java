package co.com.perficient.project3.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final UUID uuidA = UUID.fromString("1852622d-e698-41e9-b682-cf04a5ccf280");
    public static final UUID uuidB = UUID.fromString("f09be61e-682b-46f5-8905-0cd71151063e");

    public static final String COUNTRY = "/country";
    public static final String SIGNUP = "/signup";
    public static final String SIGNIN = "/signin";
    public static final String LAST = "/last";

    public static final String BIRTHDATE_JSONPATH = "$.birthDate";
    public static final String COUNTRY_JSONPATH = "$.country";
    public static final String NAME_JSONPATH = "$.name";
    public static final String NATIONALITY_JSONPATH = "$.nationality";
}
