package ro.pub.cs.systems.eim.practicaltest02;

public class BitcoinInfo {
    String updated;
    String valueEUR;
    String valueUSD;
    String info;

    public BitcoinInfo(String updated, String valueEUR, String valueUSD) {
        this.updated = updated;
        this.valueEUR = valueEUR;
        this.valueUSD = valueUSD;
    }

    public BitcoinInfo(String info) {
        this.info = info;
    }
}
