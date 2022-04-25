package org.example.luxuryhotel.framework.data;

public class Sort {
    private StringBuilder sqlOrder;
    private Sort(StringBuilder sqlOrder){
        this.sqlOrder = new StringBuilder(sqlOrder);
    }

    public StringBuilder getSqlOrder() {
        return sqlOrder;
    }

    public static Sort by(Direction direction, String column){
        StringBuilder sb = new StringBuilder();
        sb.append(column).append(" ").append(direction.name());
        return new Sort(sb);
    }
    public Sort and(Sort sort){
        this.sqlOrder.append(" , ").append(sort.sqlOrder);
        return this;
    }
    public static enum Direction {
        ASC,
        DESC;
    }
}
