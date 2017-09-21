package ru.tinkoff.interview.infrastructure;

public class Constant {

    public static class Url {
        public static final String START_PAGE = "https://www.tinkoff.ru";
        public static final String PAYMENTS_PAGE = START_PAGE + "/payments/";
        public static final String SERVICE_PROVIDER_PAGE = START_PAGE + "/zhku-moskva/";
    }

    public static class Common {
        public static final long TEST_TIMEOUT_MS = 60_000;
    }
}
