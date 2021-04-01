package beercloak;

public enum Drunkenness
{

    SOBER(0),
    TIPSY(1),
    DRUNK(2),
    WASTED(3),
    KAPUTT(4);

    public static Drunkenness drunk(float abv, int qty)
    {
        return qty == 0 ? SOBER : Drunkenness.values()[Integer.min(KAPUTT.value, (int)Math.floor(qty * abv / 4.7 / 10 * (KAPUTT.value - 1) + 1))];
    }

    private final int value;

    Drunkenness(int value)
    {
        this.value = value;
    }

    int value()
    {
        return this.value;
    }

}
