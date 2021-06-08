package org.projects.centralpoint.Defines;

import java.util.Arrays;

public final class WorldCountries
{
    /*
     * The order in the enum must be the same as
     * the order of the countries in the array.
     */

    public enum CountryIndex
    {
        AFGHANISTAN, ALBANIA, ALGERIA, ANDORRA, ANGOLA, ANTIGUA_AND_BARBUDA,
        ARGENTINA, ARMENIA, AUSTRALIA, AUSTRIA, AZERBAIJAN, BAHAMAS, BAHRAIN,
        BANGLADESH, BARBADOS, BELARUS, BELGIUM, BELIZE, BENIN, BHUTAN, BOLIVIA, BOSNIA_AND_HERZEGOVINA,
        BOTSWANA, BRAZIL, BRUNEI, BULGARIA, BURKINA_FASO, BURUNDI, CÔTE_DIVOIRE, CABO_VERDE, CAMBODIA,
        CAMEROON, CANADA, CENTRAL_AFRICAN_REPUBLIC, CHAD, CHILE, CHINA, COLOMBIA, COMOROS, CONGO,
        COSTA_RICA, CROATIA, CUBA, CYPRUS, CZECH_REPUBLIC, DENMARK, DOMINICA, DOMINICAN_REPUBLIC,
        ECUADOR, EGYPT, EL_SALVADOR, EQUATORIAL_GUINEA, ERITREA, ESTONIA, ETHIOPIA, FIJI,
        FINLAND, FRANCE, GABON, GAMBIA, GEORGIA, GERMANY, GHANA, GREECE, GRENADA, GUATEMALA,
        GUINEA, GUINEA_BISSAU, GUYANA, HAITI, VATICAN, HONDURAS, HUNGARY, ICELAND, INDIA,
        INDONESIA, IRAN, IRAQ, IRELAND, ISRAEL, ITALY, JAMAICA, JAPAN, JORDAN, KAZAKHSTAN,
        KENYA, KIRIBATI, KUWAIT, KYRGYZSTAN, LAOS, LATVIA, LEBANON, LESOTHO, LIBERIA, LIBYA,
        LIECHTENSTEIN, LITHUANIA, LUXEMBOURG, MACEDONIA, MADAGASCAR, MALAWI, MALAYSIA, MALDIVES,
        MALI, MALTA, MARSHALL_ISLANDS, MAURITANIA, MAURITIUS, MEXICO, MICRONESIA, MOLDOVA, MONACO,
        MONGOLIA, MONTENEGRO, MOROCCO, MOZAMBIQUE, MYANMAR, NAMIBIA, NAURU, NEPAL, NETHERLANDS,
        NEW_ZEALAND, NICARAGUA, NIGER, NIGERIA, NORTH_KOREA, NORWAY, OMAN, PAKISTAN, PALAU,
        PALESTINE, PANAMA, PAPUA_NEW_GUINEA, PARAGUAY, PERU, PHILIPPINES, POLAND, PORTUGAL,
        QATAR, ROMANIA, RUSSIA, RWANDA, SAMOA, SAN_MARINO, SAO_TOME_AND_PRINCIPE, SAUDI_ARABIA,
        SENEGAL, SERBIA, SEYCHELLES, SIERRA_LEONE, SINGAPORE, SLOVAKIA, SLOVENIA, SOLOMON_ISLANDS,
        SOMALIA, SOUTH_AFRICA, SOUTH_KOREA, SOUTH_SUDAN, SPAIN, SRI_LANKA, SUDAN, SWAZILAND, SWEDEN,
        SWITZERLAND, SYRIA, TAJIKISTAN, TANZANIA, THAILAND, TOGO, TONGA, TRINIDAD_AND_TOBAGO, TUNISIA,
        TURKEY, TURKMENISTAN, TUVALU, UGANDA, UKRAINE, UNITED_ARAB_EMIRATES, UNITED_KINGDOM,
        UNITED_STATES_OF_AMERICA, URUGUAY, UZBEKISTAN, VANUATU, VENEZUELA, VIETNAM, YEMEN, ZAMBIA, ZIMBABWE
    }

    public static String[] getAllCountries() { return countries; }
    public static String getCountryByIndex(CountryIndex ind) { return countries[ind.ordinal()]; }
    public static boolean isValidCountry(String str)
    {
        return Arrays.asList(countries).contains(str);
    }

    private static final String[] countries = {"afghanistan", "albania", "algeria", "andorra", "angola", "antigua and barbuda",
            "argentina", "armenia", "australia", "austria", "azerbaijan", "bahamas", "bahrain",
            "bangladesh", "barbados", "belarus", "belgium", "belize", "benin", "bhutan", "bolivia", "bosnia and herzegovina",
            "botswana", "brazil", "brunei", "bulgaria", "burkina faso", "burundi", "côte d'ivoire", "cabo verde", "cambodia",
            "cameroon", "canada", "central african republic", "chad", "chile", "china", "colombia", "comoros", "congo",
            "costa rica", "croatia", "cuba", "cyprus", "czech republic", "denmark", "dominica", "dominican republic",
            "ecuador", "egypt", "el salvador", "equatorial guinea", "eritrea", "estonia", "ethiopia", "fiji",
            "finland", "france", "gabon", "gambia", "georgia", "germany", "ghana", "greece", "grenada", "guatemala",
            "guinea", "guinea-bissau", "guyana", "haiti", "holy see", "honduras", "hungary", "iceland", "india",
            "indonesia", "iran", "iraq", "ireland", "israel", "italy", "jamaica", "japan", "jordan", "kazakhstan",
            "kenya", "kiribati", "kuwait", "kyrgyzstan", "laos", "latvia", "lebanon", "lesotho", "liberia", "libya",
            "liechtenstein", "lithuania", "luxembourg", "macedonia", "madagascar", "malawi", "malaysia", "maldives",
            "mali", "malta", "marshall islands", "mauritania", "mauritius", "mexico", "micronesia", "moldova", "monaco",
            "mongolia", "montenegro", "morocco", "mozambique", "myanmar", "namibia", "nauru", "nepal", "netherlands",
            "new zealand", "nicaragua", "niger", "nigeria", "north korea", "norway", "oman", "pakistan", "palau",
            "palestine state", "panama", "papua new guinea", "paraguay", "peru", "philippines", "poland", "portugal",
            "qatar", "romania", "russia", "rwanda", "samoa", "san marino", "sao tome and principe", "saudi arabia",
            "senegal", "serbia", "seychelles", "sierra leone", "singapore", "slovakia", "slovenia", "solomon islands",
            "somalia", "south africa", "south korea", "south sudan", "spain", "sri lanka", "sudan", "swaziland", "sweden",
            "switzerland", "syria", "tajikistan", "tanzania", "thailand", "togo", "tonga", "trinidad and tobago", "tunisia",
            "turkey", "turkmenistan", "tuvalu", "uganda", "ukraine", "united arab emirates", "united kingdom",
            "united states of america", "uruguay", "uzbekistan", "vanuatu", "venezuela", "vietnam", "yemen", "zambia", "zimbabwe"};
}
