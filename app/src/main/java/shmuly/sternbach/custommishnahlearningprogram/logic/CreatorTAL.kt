package shmuly.sternbach.custommishnahlearningprogram

import java.time.*
import java.time.temporal.*
import kotlin.math.*

const val TOTAL_MISHNAYOT = 4192
val Sedarim = listOf("Zeraim", "Moed", "Nashim", "Nezikin", "Kodashim", "Taharos")


val Masechtot = listOf(
    "Berachot",
    "Peah",
    "Damai",
    "Kilaim",
    "Sheviit",
    "Terumot",
    "Maserot",
    "Maser Sheni",
    "Chalah",
    "Orlah",
    "Bikurim",
    "Shabbat",
    "Eruvin",
    "Psachim",
    "Shekalim",
    "Yoma",
    "Sukkah",
    "Beitzah",
    "Rosh HaShanah",
    "Taanit",
    "Megilah",
    "Moed Kattan",
    "Chaggigah",
    "Yevamot",
    "Ketubot",
    "Nedarim",
    "Nazir",
    "Sotah",
    "Gittin",
    "Kidushin",
    "Bava Kamma",
    "Bava Metzia",
    "Bava Batra",
    "Sanhedrin",
    "Makot",
    "Shevuot",
    "Eduyot",
    "Avodah Zara",
    "Avot",
    "Horyot",
    "Zevachim",
    "Menachot",
    "Chullin",
    "Bechorot",
    "Erchin",
    "Temurah",
    "Keritot",
    "Meilah",
    "Tamid",
    "Middot",
    "Kinnim",
    "Kelim",
    "Ohalot",
    "Negaim",
    "Parah",
    "Taharot",
    "Mikvaot",
    "Niddah",
    "Machshirin",
    "Zavim",
    "Tevul Yom",
    "Yadayim",
    "Uktzim"
)
val Perakim = listOf(
    listOf(5, 8, 6, 7, 5, 8, 5, 8, 5),
    listOf(6, 8, 8, 11, 8, 11, 8, 9),
    listOf(4, 5, 6, 7, 11, 12, 8),
    listOf(9, 11, 7, 9, 8, 9, 8, 6, 10),
    listOf(8, 10, 10, 10, 9, 6, 7, 11, 9, 9),
    listOf(10, 6, 9, 13, 9, 6, 7, 12, 7, 12, 10),
    listOf(8, 8, 10, 6, 8),
    listOf(7, 10, 13, 12, 15),
    listOf(9, 8, 10, 11),
    listOf(9, 17, 9),
    listOf(11, 11, 12, 5),
    listOf(11, 7, 6, 2, 4, 10, 4, 7, 7, 6, 6, 6, 7, 4, 3, 8, 8, 3, 6, 5, 3, 6, 5, 5),
    listOf(10, 6, 9, 11, 9, 10, 11, 11, 4, 15),
    listOf(7, 8, 8, 9, 10, 6, 13, 8, 11, 9),
    listOf(7, 5, 4, 9, 6, 6, 7, 8),
    listOf(8, 7, 11, 6, 7, 8, 5, 9),
    listOf(11, 9, 15, 10, 8),
    listOf(10, 10, 8, 7, 7),
    listOf(9, 8, 9, 9),
    listOf(7, 10, 9, 8),
    listOf(11, 6, 6, 10),
    listOf(10, 5, 9),
    listOf(8, 7, 8),
    listOf(4, 10, 10, 13, 6, 6, 6, 6, 6, 9, 7, 6, 13, 9, 10, 7),
    listOf(10, 10, 9, 12, 9, 7, 10, 8, 9, 6, 6, 4, 11),
    listOf(4, 5, 11, 8, 6, 10, 9, 7, 10, 8, 12),
    listOf(7, 10, 7, 7, 7, 11, 4, 2, 5),
    listOf(9, 6, 8, 5, 5, 4, 8, 7, 15),
    listOf(6, 7, 8, 9, 9, 7, 9, 10, 10),
    listOf(10, 10, 13, 14),
    listOf(4, 6, 11, 9, 7, 6, 7, 7, 12, 10),
    listOf(8, 11, 12, 12, 11, 8, 11, 9, 13, 6),
    listOf(6, 14, 8, 9, 11, 8, 4, 8, 10, 8),
    listOf(6, 5, 8, 5, 5, 6, 11, 7, 6, 6, 6),
    listOf(10, 8, 16),
    listOf(7, 5, 11, 13, 5, 7, 8, 6),
    listOf(14, 10, 12, 12, 7, 3, 9, 7),
    listOf(9, 7, 10, 12, 12),
    listOf(18, 16, 18, 22, 23, 11),
    listOf(5, 7, 8),
    listOf(4, 5, 6, 6, 8, 7, 6, 12, 7, 8, 8, 6, 8, 10),
    listOf(4, 5, 7, 5, 9, 7, 6, 7, 9, 9, 9, 5, 11),
    listOf(7, 10, 7, 7, 5, 7, 6, 6, 8, 4, 2, 5),
    listOf(7, 9, 4, 10, 6, 12, 7, 10, 8),
    listOf(4, 6, 5, 4, 6, 5, 5, 7, 8),
    listOf(6, 3, 5, 4, 6, 5, 6),
    listOf(7, 6, 10, 3, 8, 9),
    listOf(4, 9, 8, 6, 5, 6),
    listOf(4, 5, 9, 3, 6, 4, 3),
    listOf(9, 6, 8, 7, 4),
    listOf(4, 5, 6),
    listOf(
        9,
        8,
        8,
        4,
        11,
        4,
        6,
        11,
        8,
        8,
        9,
        8,
        8,
        8,
        6,
        8,
        17,
        9,
        10,
        7,
        3,
        10,
        5,
        17,
        9,
        9,
        12,
        10,
        8,
        4
    ),
    listOf(8, 7, 7, 3, 7, 7, 6, 6, 16, 7, 9, 8, 6, 7, 10, 5, 5, 10),
    listOf(6, 5, 8, 11, 5, 8, 5, 10, 3, 10, 12, 7, 12, 13),
    listOf(4, 5, 11, 4, 9, 5, 12, 11, 9, 6, 9, 11),
    listOf(9, 8, 8, 13, 9, 10, 9, 9, 9, 8),
    listOf(8, 10, 4, 5, 6, 11, 7, 5, 7, 8),
    listOf(7, 7, 7, 7, 9, 14, 5, 4, 11, 8),
    listOf(6, 11, 8, 10, 11, 8),
    listOf(6, 4, 3, 7, 12),
    listOf(5, 8, 6, 7),
    listOf(5, 4, 5, 8),
    listOf(6, 10, 12)
)
val IDtags = listOf(
    "Berachot, 1" to 0,
    "Berachot, 2" to 5,
    "Berachot, 3" to 13,
    "Berachot, 4" to 19,
    "Berachot, 5" to 26,
    "Berachot, 6" to 31,
    "Berachot, 7" to 39,
    "Berachot, 8" to 44,
    "Berachot, 9" to 52,
    "Peah, 1" to 57,
    "Peah, 2" to 63,
    "Peah, 3" to 71,
    "Peah, 4" to 79,
    "Peah, 5" to 90,
    "Peah, 6" to 98,
    "Peah, 7" to 109,
    "Peah, 8" to 117,
    "Damai, 1" to 126,
    "Damai, 2" to 130,
    "Damai, 3" to 135,
    "Damai, 4" to 141,
    "Damai, 5" to 148,
    "Damai, 6" to 159,
    "Damai, 7" to 171,
    "Kilaim, 1" to 179,
    "Kilaim, 2" to 188,
    "Kilaim, 3" to 199,
    "Kilaim, 4" to 206,
    "Kilaim, 5" to 215,
    "Kilaim, 6" to 223,
    "Kilaim, 7" to 232,
    "Kilaim, 8" to 240,
    "Kilaim, 9" to 246,
    "Sheviit, 1" to 256,
    "Sheviit, 2" to 264,
    "Sheviit, 3" to 274,
    "Sheviit, 4" to 284,
    "Sheviit, 5" to 294,
    "Sheviit, 6" to 303,
    "Sheviit, 7" to 309,
    "Sheviit, 8" to 316,
    "Sheviit, 9" to 327,
    "Sheviit, 10" to 336,
    "Terumot, 1" to 345,
    "Terumot, 2" to 355,
    "Terumot, 3" to 361,
    "Terumot, 4" to 370,
    "Terumot, 5" to 383,
    "Terumot, 6" to 392,
    "Terumot, 7" to 398,
    "Terumot, 8" to 405,
    "Terumot, 9" to 417,
    "Terumot, 10" to 424,
    "Terumot, 11" to 436,
    "Maserot, 1" to 446,
    "Maserot, 2" to 454,
    "Maserot, 3" to 462,
    "Maserot, 4" to 472,
    "Maserot, 5" to 478,
    "Maser Sheni, 1" to 486,
    "Maser Sheni, 2" to 493,
    "Maser Sheni, 3" to 503,
    "Maser Sheni, 4" to 516,
    "Maser Sheni, 5" to 528,
    "Chalah, 1" to 543,
    "Chalah, 2" to 552,
    "Chalah, 3" to 560,
    "Chalah, 4" to 570,
    "Orlah, 1" to 581,
    "Orlah, 2" to 590,
    "Orlah, 3" to 607,
    "Bikurim, 1" to 616,
    "Bikurim, 2" to 627,
    "Bikurim, 3" to 638,
    "Bikurim, 4" to 650,
    "Shabbat, 1" to 655,
    "Shabbat, 2" to 666,
    "Shabbat, 3" to 673,
    "Shabbat, 4" to 679,
    "Shabbat, 5" to 681,
    "Shabbat, 6" to 685,
    "Shabbat, 7" to 695,
    "Shabbat, 8" to 699,
    "Shabbat, 9" to 706,
    "Shabbat, 10" to 713,
    "Shabbat, 11" to 719,
    "Shabbat, 12" to 725,
    "Shabbat, 13" to 731,
    "Shabbat, 14" to 738,
    "Shabbat, 15" to 742,
    "Shabbat, 16" to 745,
    "Shabbat, 17" to 753,
    "Shabbat, 18" to 761,
    "Shabbat, 19" to 764,
    "Shabbat, 20" to 770,
    "Shabbat, 21" to 775,
    "Shabbat, 22" to 778,
    "Shabbat, 23" to 784,
    "Shabbat, 24" to 789,
    "Eruvin, 1" to 794,
    "Eruvin, 2" to 804,
    "Eruvin, 3" to 810,
    "Eruvin, 4" to 819,
    "Eruvin, 5" to 830,
    "Eruvin, 6" to 839,
    "Eruvin, 7" to 849,
    "Eruvin, 8" to 860,
    "Eruvin, 9" to 871,
    "Eruvin, 10" to 875,
    "Psachim, 1" to 890,
    "Psachim, 2" to 897,
    "Psachim, 3" to 905,
    "Psachim, 4" to 913,
    "Psachim, 5" to 922,
    "Psachim, 6" to 932,
    "Psachim, 7" to 938,
    "Psachim, 8" to 951,
    "Psachim, 9" to 959,
    "Psachim, 10" to 970,
    "Shekalim, 1" to 979,
    "Shekalim, 2" to 986,
    "Shekalim, 3" to 991,
    "Shekalim, 4" to 995,
    "Shekalim, 5" to 1004,
    "Shekalim, 6" to 1010,
    "Shekalim, 7" to 1016,
    "Shekalim, 8" to 1023,
    "Yoma, 1" to 1031,
    "Yoma, 2" to 1039,
    "Yoma, 3" to 1046,
    "Yoma, 4" to 1057,
    "Yoma, 5" to 1063,
    "Yoma, 6" to 1070,
    "Yoma, 7" to 1078,
    "Yoma, 8" to 1083,
    "Sukkah, 1" to 1092,
    "Sukkah, 2" to 1103,
    "Sukkah, 3" to 1112,
    "Sukkah, 4" to 1127,
    "Sukkah, 5" to 1137,
    "Beitzah, 1" to 1145,
    "Beitzah, 2" to 1155,
    "Beitzah, 3" to 1165,
    "Beitzah, 4" to 1173,
    "Beitzah, 5" to 1180,
    "Rosh HaShanah, 1" to 1187,
    "Rosh HaShanah, 2" to 1196,
    "Rosh HaShanah, 3" to 1204,
    "Rosh HaShanah, 4" to 1213,
    "Taanit, 1" to 1222,
    "Taanit, 2" to 1229,
    "Taanit, 3" to 1239,
    "Taanit, 4" to 1248,
    "Megilah, 1" to 1256,
    "Megilah, 2" to 1267,
    "Megilah, 3" to 1273,
    "Megilah, 4" to 1279,
    "Moed Kattan, 1" to 1289,
    "Moed Kattan, 2" to 1299,
    "Moed Kattan, 3" to 1304,
    "Chaggigah, 1" to 1313,
    "Chaggigah, 2" to 1321,
    "Chaggigah, 3" to 1328,
    "Yevamot, 1" to 1336,
    "Yevamot, 2" to 1340,
    "Yevamot, 3" to 1350,
    "Yevamot, 4" to 1360,
    "Yevamot, 5" to 1373,
    "Yevamot, 6" to 1379,
    "Yevamot, 7" to 1385,
    "Yevamot, 8" to 1391,
    "Yevamot, 9" to 1397,
    "Yevamot, 10" to 1403,
    "Yevamot, 11" to 1412,
    "Yevamot, 12" to 1419,
    "Yevamot, 13" to 1425,
    "Yevamot, 14" to 1438,
    "Yevamot, 15" to 1447,
    "Yevamot, 16" to 1457,
    "Ketubot, 1" to 1464,
    "Ketubot, 2" to 1474,
    "Ketubot, 3" to 1484,
    "Ketubot, 4" to 1493,
    "Ketubot, 5" to 1505,
    "Ketubot, 6" to 1514,
    "Ketubot, 7" to 1521,
    "Ketubot, 8" to 1531,
    "Ketubot, 9" to 1539,
    "Ketubot, 10" to 1548,
    "Ketubot, 11" to 1554,
    "Ketubot, 12" to 1560,
    "Ketubot, 13" to 1564,
    "Nedarim, 1" to 1575,
    "Nedarim, 2" to 1579,
    "Nedarim, 3" to 1584,
    "Nedarim, 4" to 1595,
    "Nedarim, 5" to 1603,
    "Nedarim, 6" to 1609,
    "Nedarim, 7" to 1619,
    "Nedarim, 8" to 1628,
    "Nedarim, 9" to 1635,
    "Nedarim, 10" to 1645,
    "Nedarim, 11" to 1653,
    "Nazir, 1" to 1665,
    "Nazir, 2" to 1672,
    "Nazir, 3" to 1682,
    "Nazir, 4" to 1689,
    "Nazir, 5" to 1696,
    "Nazir, 6" to 1703,
    "Nazir, 7" to 1714,
    "Nazir, 8" to 1718,
    "Nazir, 9" to 1720,
    "Sotah, 1" to 1725,
    "Sotah, 2" to 1734,
    "Sotah, 3" to 1740,
    "Sotah, 4" to 1748,
    "Sotah, 5" to 1753,
    "Sotah, 6" to 1758,
    "Sotah, 7" to 1762,
    "Sotah, 8" to 1770,
    "Sotah, 9" to 1777,
    "Gittin, 1" to 1792,
    "Gittin, 2" to 1798,
    "Gittin, 3" to 1805,
    "Gittin, 4" to 1813,
    "Gittin, 5" to 1822,
    "Gittin, 6" to 1831,
    "Gittin, 7" to 1838,
    "Gittin, 8" to 1847,
    "Gittin, 9" to 1857,
    "Kidushin, 1" to 1867,
    "Kidushin, 2" to 1877,
    "Kidushin, 3" to 1887,
    "Kidushin, 4" to 1900,
    "Bava Kamma, 1" to 1914,
    "Bava Kamma, 2" to 1918,
    "Bava Kamma, 3" to 1924,
    "Bava Kamma, 4" to 1935,
    "Bava Kamma, 5" to 1944,
    "Bava Kamma, 6" to 1951,
    "Bava Kamma, 7" to 1957,
    "Bava Kamma, 8" to 1964,
    "Bava Kamma, 9" to 1971,
    "Bava Kamma, 10" to 1983,
    "Bava Metzia, 1" to 1993,
    "Bava Metzia, 2" to 2001,
    "Bava Metzia, 3" to 2012,
    "Bava Metzia, 4" to 2024,
    "Bava Metzia, 5" to 2036,
    "Bava Metzia, 6" to 2047,
    "Bava Metzia, 7" to 2055,
    "Bava Metzia, 8" to 2066,
    "Bava Metzia, 9" to 2075,
    "Bava Metzia, 10" to 2088,
    "Bava Batra, 1" to 2094,
    "Bava Batra, 2" to 2100,
    "Bava Batra, 3" to 2114,
    "Bava Batra, 4" to 2122,
    "Bava Batra, 5" to 2131,
    "Bava Batra, 6" to 2142,
    "Bava Batra, 7" to 2150,
    "Bava Batra, 8" to 2154,
    "Bava Batra, 9" to 2162,
    "Bava Batra, 10" to 2172,
    "Sanhedrin, 1" to 2180,
    "Sanhedrin, 2" to 2186,
    "Sanhedrin, 3" to 2191,
    "Sanhedrin, 4" to 2199,
    "Sanhedrin, 5" to 2204,
    "Sanhedrin, 6" to 2209,
    "Sanhedrin, 7" to 2215,
    "Sanhedrin, 8" to 2226,
    "Sanhedrin, 9" to 2233,
    "Sanhedrin, 10" to 2239,
    "Sanhedrin, 11" to 2245,
    "Makot, 1" to 2251,
    "Makot, 2" to 2261,
    "Makot, 3" to 2269,
    "Shevuot, 1" to 2285,
    "Shevuot, 2" to 2292,
    "Shevuot, 3" to 2297,
    "Shevuot, 4" to 2308,
    "Shevuot, 5" to 2321,
    "Shevuot, 6" to 2326,
    "Shevuot, 7" to 2333,
    "Shevuot, 8" to 2341,
    "Eduyot, 1" to 2347,
    "Eduyot, 2" to 2361,
    "Eduyot, 3" to 2371,
    "Eduyot, 4" to 2383,
    "Eduyot, 5" to 2395,
    "Eduyot, 6" to 2402,
    "Eduyot, 7" to 2405,
    "Eduyot, 8" to 2414,
    "Avodah Zara, 1" to 2421,
    "Avodah Zara, 2" to 2430,
    "Avodah Zara, 3" to 2437,
    "Avodah Zara, 4" to 2447,
    "Avodah Zara, 5" to 2459,
    "Avot, 1" to 2471,
    "Avot, 2" to 2489,
    "Avot, 3" to 2505,
    "Avot, 4" to 2523,
    "Avot, 5" to 2545,
    "Avot, 6" to 2568,
    "Horyot, 1" to 2579,
    "Horyot, 2" to 2584,
    "Horyot, 3" to 2591,
    "Zevachim, 1" to 2599,
    "Zevachim, 2" to 2603,
    "Zevachim, 3" to 2608,
    "Zevachim, 4" to 2614,
    "Zevachim, 5" to 2620,
    "Zevachim, 6" to 2628,
    "Zevachim, 7" to 2635,
    "Zevachim, 8" to 2641,
    "Zevachim, 9" to 2653,
    "Zevachim, 10" to 2660,
    "Zevachim, 11" to 2668,
    "Zevachim, 12" to 2676,
    "Zevachim, 13" to 2682,
    "Zevachim, 14" to 2690,
    "Menachot, 1" to 2700,
    "Menachot, 2" to 2704,
    "Menachot, 3" to 2709,
    "Menachot, 4" to 2716,
    "Menachot, 5" to 2721,
    "Menachot, 6" to 2730,
    "Menachot, 7" to 2737,
    "Menachot, 8" to 2743,
    "Menachot, 9" to 2750,
    "Menachot, 10" to 2759,
    "Menachot, 11" to 2768,
    "Menachot, 12" to 2777,
    "Menachot, 13" to 2782,
    "Chullin, 1" to 2793,
    "Chullin, 2" to 2800,
    "Chullin, 3" to 2810,
    "Chullin, 4" to 2817,
    "Chullin, 5" to 2824,
    "Chullin, 6" to 2829,
    "Chullin, 7" to 2836,
    "Chullin, 8" to 2842,
    "Chullin, 9" to 2848,
    "Chullin, 10" to 2856,
    "Chullin, 11" to 2860,
    "Chullin, 12" to 2862,
    "Bechorot, 1" to 2867,
    "Bechorot, 2" to 2874,
    "Bechorot, 3" to 2883,
    "Bechorot, 4" to 2887,
    "Bechorot, 5" to 2897,
    "Bechorot, 6" to 2903,
    "Bechorot, 7" to 2915,
    "Bechorot, 8" to 2922,
    "Bechorot, 9" to 2932,
    "Erchin, 1" to 2940,
    "Erchin, 2" to 2944,
    "Erchin, 3" to 2950,
    "Erchin, 4" to 2955,
    "Erchin, 5" to 2959,
    "Erchin, 6" to 2965,
    "Erchin, 7" to 2970,
    "Erchin, 8" to 2975,
    "Erchin, 9" to 2982,
    "Temurah, 1" to 2990,
    "Temurah, 2" to 2996,
    "Temurah, 3" to 2999,
    "Temurah, 4" to 3004,
    "Temurah, 5" to 3008,
    "Temurah, 6" to 3014,
    "Temurah, 7" to 3019,
    "Keritot, 1" to 3025,
    "Keritot, 2" to 3032,
    "Keritot, 3" to 3038,
    "Keritot, 4" to 3048,
    "Keritot, 5" to 3051,
    "Keritot, 6" to 3059,
    "Meilah, 1" to 3068,
    "Meilah, 2" to 3072,
    "Meilah, 3" to 3081,
    "Meilah, 4" to 3089,
    "Meilah, 5" to 3095,
    "Meilah, 6" to 3100,
    "Tamid, 1" to 3106,
    "Tamid, 2" to 3110,
    "Tamid, 3" to 3115,
    "Tamid, 4" to 3124,
    "Tamid, 5" to 3127,
    "Tamid, 6" to 3133,
    "Tamid, 7" to 3137,
    "Middot, 1" to 3140,
    "Middot, 2" to 3149,
    "Middot, 3" to 3155,
    "Middot, 4" to 3163,
    "Middot, 5" to 3170,
    "Kinnim, 1" to 3174,
    "Kinnim, 2" to 3178,
    "Kinnim, 3" to 3183,
    "Kelim, 1" to 3189,
    "Kelim, 2" to 3198,
    "Kelim, 3" to 3206,
    "Kelim, 4" to 3214,
    "Kelim, 5" to 3218,
    "Kelim, 6" to 3229,
    "Kelim, 7" to 3233,
    "Kelim, 8" to 3239,
    "Kelim, 9" to 3250,
    "Kelim, 10" to 3258,
    "Kelim, 11" to 3266,
    "Kelim, 12" to 3275,
    "Kelim, 13" to 3283,
    "Kelim, 14" to 3291,
    "Kelim, 15" to 3299,
    "Kelim, 16" to 3305,
    "Kelim, 17" to 3313,
    "Kelim, 18" to 3330,
    "Kelim, 19" to 3339,
    "Kelim, 20" to 3349,
    "Kelim, 21" to 3356,
    "Kelim, 22" to 3359,
    "Kelim, 23" to 3369,
    "Kelim, 24" to 3374,
    "Kelim, 25" to 3391,
    "Kelim, 26" to 3400,
    "Kelim, 27" to 3409,
    "Kelim, 28" to 3421,
    "Kelim, 29" to 3431,
    "Kelim, 30" to 3439,
    "Ohalot, 1" to 3443,
    "Ohalot, 2" to 3451,
    "Ohalot, 3" to 3458,
    "Ohalot, 4" to 3465,
    "Ohalot, 5" to 3468,
    "Ohalot, 6" to 3475,
    "Ohalot, 7" to 3482,
    "Ohalot, 8" to 3488,
    "Ohalot, 9" to 3494,
    "Ohalot, 10" to 3510,
    "Ohalot, 11" to 3517,
    "Ohalot, 12" to 3526,
    "Ohalot, 13" to 3534,
    "Ohalot, 14" to 3540,
    "Ohalot, 15" to 3547,
    "Ohalot, 16" to 3557,
    "Ohalot, 17" to 3562,
    "Ohalot, 18" to 3567,
    "Negaim, 1" to 3577,
    "Negaim, 2" to 3583,
    "Negaim, 3" to 3588,
    "Negaim, 4" to 3596,
    "Negaim, 5" to 3607,
    "Negaim, 6" to 3612,
    "Negaim, 7" to 3620,
    "Negaim, 8" to 3625,
    "Negaim, 9" to 3635,
    "Negaim, 10" to 3638,
    "Negaim, 11" to 3648,
    "Negaim, 12" to 3660,
    "Negaim, 13" to 3667,
    "Negaim, 14" to 3679,
    "Parah, 1" to 3692,
    "Parah, 2" to 3696,
    "Parah, 3" to 3701,
    "Parah, 4" to 3712,
    "Parah, 5" to 3716,
    "Parah, 6" to 3725,
    "Parah, 7" to 3730,
    "Parah, 8" to 3742,
    "Parah, 9" to 3753,
    "Parah, 10" to 3762,
    "Parah, 11" to 3768,
    "Parah, 12" to 3777,
    "Taharot, 1" to 3788,
    "Taharot, 2" to 3797,
    "Taharot, 3" to 3805,
    "Taharot, 4" to 3813,
    "Taharot, 5" to 3826,
    "Taharot, 6" to 3835,
    "Taharot, 7" to 3845,
    "Taharot, 8" to 3854,
    "Taharot, 9" to 3863,
    "Taharot, 10" to 3872,
    "Mikvaot, 1" to 3880,
    "Mikvaot, 2" to 3888,
    "Mikvaot, 3" to 3898,
    "Mikvaot, 4" to 3902,
    "Mikvaot, 5" to 3907,
    "Mikvaot, 6" to 3913,
    "Mikvaot, 7" to 3924,
    "Mikvaot, 8" to 3931,
    "Mikvaot, 9" to 3936,
    "Mikvaot, 10" to 3943,
    "Niddah, 1" to 3951,
    "Niddah, 2" to 3958,
    "Niddah, 3" to 3965,
    "Niddah, 4" to 3972,
    "Niddah, 5" to 3979,
    "Niddah, 6" to 3988,
    "Niddah, 7" to 4002,
    "Niddah, 8" to 4007,
    "Niddah, 9" to 4011,
    "Niddah, 10" to 4022,
    "Machshirin, 1" to 4030,
    "Machshirin, 2" to 4036,
    "Machshirin, 3" to 4047,
    "Machshirin, 4" to 4055,
    "Machshirin, 5" to 4065,
    "Machshirin, 6" to 4076,
    "Zavim, 1" to 4084,
    "Zavim, 2" to 4090,
    "Zavim, 3" to 4094,
    "Zavim, 4" to 4097,
    "Zavim, 5" to 4104,
    "Tevul Yom, 1" to 4116,
    "Tevul Yom, 2" to 4121,
    "Tevul Yom, 3" to 4129,
    "Tevul Yom, 4" to 4135,
    "Yadayim, 1" to 4142,
    "Yadayim, 2" to 4147,
    "Yadayim, 3" to 4151,
    "Yadayim, 4" to 4156,
    "Uktzim, 1" to 4164,
    "Uktzim, 2" to 4170,
    "Uktzim, 3" to 4180
)
val SederTags = listOf(
    "Zeraim" to 655 trio 0,
    "Moed" to 681 trio 655,
    "Nashim" to 578 trio 1336,
    "Nezikin" to 685 trio 1914,
    "Kodshim" to 590 trio 2599,
    "Taharot" to 1003 trio 3189
)

fun main() {
    getCalendar(









    )
    //printChart(chart)
}

fun getCalendar(
    startM: String = "Yevamot, 1,1",
    end: String = endOf("Horyot"),
    unit: Pair<Int, String> = Pair(14, "Mishnah"),
    notOn: List<Int> = listOf(),
    startD: LocalDate = LocalDate.of(2021, 3, 1),
    per: Period = Period.of(0, 0, 7),
    increment: Period = Period.of(0, 0, 1),
    restDay: Period = Period.of(0, 0, 0),
    dayExempt: MutableList<LocalDate> = mutableListOf()
): List<Triple<LocalDate, Pair<String, String>, Pair<String, String>>> {
    val chart = setChart(startM, unit, end)
    return dividedInto(
        chart,
        setDays(chart.size, startD, per, restDay, notOn, dayExempt), //calendar
        per,
        increment,
        startD,
        restDay,
        notOn,
        dayExempt
    )
}

fun endOf(name: String): String {
    val list = Perakim[Masechtot.indexOf(name)]
    return "${name}, ${list.size},${
        list[list.size - 1]
    }"
}

fun setChart(
    startM: String = "Berachot, 1,1",
    unit: Pair<Int, String> = Pair(1, "Mishnah"),
    end: String = ""
): List<Triple<Int, Pair<String, String>, Pair<String, String>>> {
    val finalChart = mutableListOf<Triple<Int, Pair<String, String>, Pair<String, String>>>()
    var count = IDtags.toMap().getValue(startM.substring(0, startM.lastIndexOf(",")))
    val endIndex = if (end == "") {
        TOTAL_MISHNAYOT - count
    } else {
        (findMishnahIndex(end) - count).absoluteValue
    }
    var i = 0
    var j = 0
    while (i < endIndex) {
        val firstM: Pair<String, String> = findMishnahString(count)
        count += incrementSize(
            count,
            unit
        ) - 1 // need the -1 so that the last mishna of triple A will be one less that first mishna of triple B
        i += incrementSize(count, unit) // this increases by at least one; will end the loop
        val lastM: Pair<String, String> =
            if (count < findMishnahIndex(end)) findMishnahString(count) else findMishnahString(
                findMishnahIndex(end)
            )
        finalChart.add(Triple(j, firstM, lastM))
        count += 1
        j += 1
    }
    return finalChart
}

fun incrementSize(count: Int, unit: Pair<Int, String>): Int {
    return when (unit.second) {
        "Mishnah" -> 1 * unit.first
        "Perek" -> addAPerek(count, unit.first)
        "Masechet" -> addAMasechta(count, unit.first)
        "Seder" -> addASeder(count, unit.first)
        else -> 1
    }
}

fun addAPerek(count: Int, unit: Int): Int {
    var perekSize = 0
    var x = 0
    while (x < unit) {
        val ID = IDtags[IDtags.indexOf(IDtags.findLast { it.second <= (count + perekSize) % 4192 })]
        val perek = ID.first.substring(ID.first.lastIndexOf(",") + 2).toInt() - 1
        val Masechtah = Masechtot.indexOf(ID.first.substring(0, ID.first.lastIndexOf(",")))
        perekSize += Perakim[Masechtah][perek]
        x++
    }
    return perekSize
}

fun addAMasechta(count: Int, unit: Int): Int {
    var masechtahSize = 0
    var x = 0
    val ID = IDtags.findLast { it.second <= count + masechtahSize }
    val Masechtah = Masechtot.indexOf(ID!!.first.substring(0, ID.first.lastIndexOf(",")))
    while (x < unit) {
        var y = 0
        val index = (Masechtah + x) % Masechtot.size
        while (y < Perakim[index].size) {
            masechtahSize += Perakim[index][y]
            y++
        }
        x++
    }
    return masechtahSize
}

fun addASeder(count: Int = 0, unit: Int = 1, nameStart: String = ""): Int {
    var sederSize = 0
    if (nameStart == "") {
        var x = 0
        while (x < unit) {
            val ID = SederTags.findLast { it.third <= count + sederSize }
            sederSize += ID!!.second
            x++
        }
    } else {
        var x = 0
        while (x < unit) {
            val startIndex = SederTags.indexOf(SederTags.find { it.first == nameStart })
            val ID = SederTags[(startIndex + x) % 6]
            sederSize += ID.second
            x++
        }
    }
    return sederSize
}

fun findMishnahIndex(name: String): Int = IDtags.toMap()
    .getValue(name.substring(0, name.lastIndexOf(","))) + name.substring(name.lastIndexOf(",") + 1)
    .toInt() - 1

fun findMishnahString(count: Int): Pair<String, String> {
    val perek = IDtags.findLast { it.second <= count }
    return Pair(
        perek!!.first.split(",").first(),
        "${perek.first.split(",")[1]},${count - perek.second + 1}"
    )
}

fun setDays(
    length: Int,
    startD: LocalDate = LocalDate.now(),
    per: Period = Period.of(0, 0, 1),
    restDay: Period = Period.of(0, 0, 0),
    notOn: List<Int>,
    daysExempt: List<LocalDate>
): List<LocalDate> {
    val dayList = mutableListOf(LocalDate.of(1, 1, 1))
    var dateDifference = length
    var i = 0
    while (i < dateDifference) {
        val notToday: Boolean = checkSkip(startD.plus(per * i), startD, restDay, notOn, daysExempt)
        if (notToday) {
            dateDifference += 1
        } else {
            dayList += startD.plus(per * i)
        }
        i++
    }
    if (dayList[0] == LocalDate.of(1, 1, 1)) dayList.removeAt(0)

    return dayList
}

fun checkSkip(
    checkDay: LocalDate,
    startD: LocalDate,
    restD: Period,
    notOn: List<Int> = listOf(),
    daysExempt: List<LocalDate> = listOf()
): Boolean {
    var test: Long = 0
    val restDay = ChronoUnit.DAYS.between(LocalDate.of(1, 1, 1), LocalDate.of(1, 1, 1) + restD)
    if (restDay != 0L) test += if (ChronoUnit.DAYS.between(
            checkDay,
            startD
        ) % restDay == 0L
    ) 1 else 0
    for (i in notOn) {
        test += if (checkDay.dayOfWeek.value == i) i else 0
    }
    for (i in daysExempt) {
        if (checkDay == i) test += 1
    }
    return test != 0L
}

fun nextDay(
    checkDay: LocalDate,
    startD: LocalDate,
    restDay: Period = Period.of(0, 0, 0),
    notOn: List<Int> = listOf(),
    daysExempt: List<LocalDate>
): LocalDate? {
    var dayList: LocalDate? = null
    var notToday = false
    val rest = ChronoUnit.DAYS.between(LocalDate.of(1, 1, 1), LocalDate.of(1, 1, 1) + restDay)
    if (rest != 0L) if (ChronoUnit.DAYS.between(checkDay, startD) % rest == 0L) notToday = true
    for (i in daysExempt) {
        if (checkDay == i) notToday = true
    }
    for (i in notOn) {
        if (checkDay.dayOfWeek.value == i) notToday = true
    }
    if (!notToday) {
        dayList = checkDay
    }

    return dayList
}

fun dividedInto(
    chart: List<Triple<Int, Pair<String, String>, Pair<String, String>>>,
    dateList: List<LocalDate>,
    per: Period,
    increment: Period,
    startD: LocalDate = LocalDate.now(),
    restDay: Period = Period.of(0, 0, 0),
    notOn: List<Int>,
    daysExempt: List<LocalDate>
): List<Triple<LocalDate, Pair<String, String>, Pair<String, String>>> {
    val finalChart = mutableListOf<Triple<LocalDate, Pair<String, String>, Pair<String, String>>>()

    var j = 0
    if (per == increment) {
        while (j < chart.size) {
            finalChart.add(Triple(dateList[j], chart[j].second, chart[j].third))
            j++
        }
        return finalChart
    } //ex: user wants 15 mishna per week and wants printed by week
    //the following is if user wants 15 mishna per week and wants printed by day
    for (i in chart) {
        val difference = ChronoUnit.DAYS.between(
            dateList[i.first],
            dateList[i.first] + per
        ) / ChronoUnit.DAYS.between(dateList[i.first], dateList[i.first] + increment)
        var y = 0
        val nullCalendar: MutableList<LocalDate?> = mutableListOf(null)
        while (y < difference) {
            val checkDay = startD + per * (i.first) + increment * y
            nullCalendar +=
                nextDay(
                    checkDay = checkDay,
                    startD = startD,
                    restDay = restDay,
                    notOn = notOn,
                    daysExempt = daysExempt
                )
            y++
        }
        val calendar = nullCalendar.filterNotNull()
        val initialMishnaIndex = findMishnahIndex(i.second.first + "," + i.second.second)
        val finalMishnaIndex = findMishnahIndex(i.third.first + "," + i.third.second)
        val incrementMishnaBy =
            ((finalMishnaIndex - initialMishnaIndex + 1).toDouble() / calendar.size)
        y = 0
        var dontTerminate = true
        while (dontTerminate) {
            val lastAddedMishnaIndex =
                if (finalChart.size == 0) initialMishnaIndex - 1 else findMishnahIndex(finalChart[finalChart.size - 1].third.first + "," + finalChart[finalChart.size - 1].third.second)
            val increaseMishnaBy =
                if (y % 2 == 0 || floor(incrementMishnaBy).toInt() == 0) ceil(incrementMishnaBy).toInt() else floor(
                    incrementMishnaBy
                ).toInt()
            val firstM: Pair<String, String> = findMishnahString(lastAddedMishnaIndex + 1)
            val lastM: Pair<String, String>
            if (increaseMishnaBy < finalMishnaIndex - lastAddedMishnaIndex && y != calendar.size - 1) {
                lastM = findMishnahString(lastAddedMishnaIndex + increaseMishnaBy)
            } else {
                lastM = findMishnahString(finalMishnaIndex)
                dontTerminate = false
            }
            finalChart.add(Triple(calendar[y], firstM, lastM))
            y++
        }
    }

    return finalChart
}


/*This functon turns string of what to increment by into a period so the program can add it*/
fun stringToPeriod(string: String, unit: Int): Period {
    return when (string.toLowerCase()) {
        "day" -> Period.of(0, 0, unit)
        "week" -> Period.of(0, 0, unit * 7)
        "month" -> Period.of(0, unit, 0)
        "year" -> Period.of(unit, 0, 0)
        else -> Period.of(0, 0, 1)
    }
}

private operator fun Period.times(i: Int): Period {
    return Period.of(this.years * i, this.months * i, this.days * i)
}

infix fun <A, B, C> Pair<A, B>.trio(that: C): Triple<A, B, C> =
    Triple(this.first, this.second, that)