package com.scdlc.kfe.DTO;

public class VentaPorMesDTO {
    private int mes;
    private long totalVenta;

    public VentaPorMesDTO(int mes, long totalVenta) {
        this.mes = mes;
        this.totalVenta = totalVenta;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public long getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(long totalVenta) {
        this.totalVenta = totalVenta;
    }

    @Override
    public String toString() {
        return "VentaPorMesDTO{" +
                "mes=" + mes +
                ", totalVenta=" + totalVenta +
                '}';
    }
}
