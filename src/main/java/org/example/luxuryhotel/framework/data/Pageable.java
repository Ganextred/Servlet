package org.example.luxuryhotel.framework.data;

public class Pageable {
    private Sort sort;
    private Integer page;
    private Integer pageCapacity;
    private Pageable(Integer page, Integer pageCapacity, Sort sort){
        this.page = page;
        this.pageCapacity = pageCapacity;
        this.sort = sort;
    };
    public static Pageable of(Integer page, Integer pageCapacity, Sort sort){
        return new Pageable(page, pageCapacity, sort);
    }
    public String upgradeStatement(String statement){
        StringBuilder sb = new StringBuilder(statement);
        sb.append(" ORDER BY ").append(sort.getSqlOrder()).append(" LIMIT ")
                .append(pageCapacity).append(" OFFSET ").append(page * pageCapacity);
        return sb.toString();
    }
}
