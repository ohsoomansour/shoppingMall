package com.shoppingmall.payment;

import lombok.Data;

@Data
public class Amount {
  private Integer total;
  private Integer tax_free;
  private Integer vat;
  private Integer point;
  private Integer discount;
}
