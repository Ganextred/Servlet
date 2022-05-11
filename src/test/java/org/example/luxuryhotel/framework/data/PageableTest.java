package org.example.luxuryhotel.framework.data;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class PageableTest {
   @Test
   public void pageableTest(){
       Sort sortPattern;
       sortPattern = Sort.by( Sort.Direction.DESC,"aaaaa");
       sortPattern.and(Sort.by( Sort.Direction.ASC, "bbbb"));
       assertEquals("aaaaaDESC,bbbbASC",sortPattern.getSqlOrder().toString().replace(" ",""));
       Pageable pageable = Pageable.of(3,20,sortPattern);
       assertEquals("ORDERBYaaaaaDESC,bbbbASCLIMIT20OFFSET60",pageable.upgradeStatement("").replace(" ",""));
   }
}