package com.example.gpbms.purchase.controller;

import com.example.gpbms.purchase.entity.purchase_item.PurchaseItemMin;
import com.example.gpbms.purchase.entity.purchase_item.TbPurchaseItem;
import com.example.gpbms.purchase.repository.TbPurchaseItemRepository;
import com.example.gpbms.util.RespBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class TbPurchaseItemController {
    @Autowired
    private TbPurchaseItemRepository tbPurchaseItemRepository;

    /**
     * 获取压缩后的采购品目目录1
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "purchase-item/min", method = RequestMethod.GET)
    public void getPurchaseItemMin(HttpServletResponse response) throws Exception {
        String jsonData = getItemMinsJson();
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonData);
    }

    /**
     * 获取压缩后的采购品目目录2
     * @return
     */
    public String getItemMinsJson() {
        List<PurchaseItemMin> itemMins = getItemMins();
        RespBean response = RespBean.success(itemMins);
        try {
            return new ObjectMapper().writeValueAsString(response);
        } catch(JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取压缩后的采购品目目录3
     * @return
     */
    public List<PurchaseItemMin> getItemMins() {
        List<PurchaseItemMin> rootItemMins = new ArrayList<>();

        Map<String, PurchaseItemMin> allItemMins = new HashMap<>();
        List<TbPurchaseItem> items = tbPurchaseItemRepository.findByOrderByCode();
        for(TbPurchaseItem item : items) {
            PurchaseItemMin itemMin = new PurchaseItemMin();
            int length = item.getCode().length() == 1 ? 0 : item.getCode().length() - 2;
            itemMin.setN(item.getCode().substring(length) + '-' + item.getName());
            allItemMins.put(item.getCode(), itemMin);
        }

        for(TbPurchaseItem item : items) {
            if(item.getCode().length() == 1) {
                // 根节点
                rootItemMins.add(allItemMins.get(item.getCode()));
            } else {
                // 有父节点
                String parentCode = item.getCode().substring(0, item.getCode().length() - 2);
                PurchaseItemMin parentItemMin = allItemMins.get(parentCode);
                if(parentItemMin.getC() == null) {
                    parentItemMin.setC(new ArrayList<>());
                }
                parentItemMin.getC().add(allItemMins.get(item.getCode()));
            }
        }
        return rootItemMins;
    }
}
