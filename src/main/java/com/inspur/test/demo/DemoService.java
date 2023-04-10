package com.inspur.test.demo;

import cn.hutool.core.util.XmlUtil;
import com.inspur.test.demo.factory.verifyInterface;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.SelfResponse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DemoService implements verifyInterface {
    /*
     * description: list steam操作
     * author: jiangyf
     * date: 2023/4/1 14:02
     * @param
     * @return util.SelfResponse
     */
    public SelfResponse arrayList() {
        List<DemoEntity> demoList = generateList(10);

        List<String> idList = demoList.stream().map(DemoEntity::getId).distinct().collect(Collectors.toList());
        // 逗号分隔
        String idComma = idList.stream().map(String::valueOf).collect(Collectors.joining(","));
        // 分号分隔
        String idSemicolon = idList.stream().map(String::valueOf).collect(Collectors.joining(";"));
        // 拼接单引号
        String idQuotation = idList.stream().map(String::valueOf).collect(Collectors.joining("','"));
        // 拼接到SQL中
        String selfSql = "SELECT ID,CODE,NAME FROM TABLE WHERE ID IN ('" + idQuotation + "')";

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("idComma", idComma);
        rtnMap.put("idSemicolon", idSemicolon);
        rtnMap.put("idQuotation", idQuotation);
        rtnMap.put("selfSql", selfSql);
        return SelfResponse.createBySuccessWithDataAndMsg("成功", rtnMap);
    }

    public SelfResponse arrayListHandel() {
        // 数据组织
        List<DemoEntity> deptListOld = generateList4Ahadle();
        List<DemoEntity> deptListNew = generateList4Bhadle();


        return SelfResponse.createBySuccessWithMsg("123");

    }


    /*
     * description: xml转换
     * author: jiangyf
     * date: 2023/4/2 9:40
     * @param json
     * @return util.SelfResponse
     */
    public SelfResponse xmlTransform(Map<String, Object> json) {
        if (StringUtils.isEmpty(json.get("proName").toString())) {
            return SelfResponse.createByErrorWithMsg("实体名称为空");
        }
        if (StringUtils.isEmpty(json.get("proData").toString())) {
            return SelfResponse.createByErrorWithMsg("实体信息为空");
        }

        JSONObject jsonObject = JSONObject.parseObject(json.get("proData").toString());
        Map tmpMap = jsonObject.toJavaObject(Map.class);
        return SelfResponse.createBySuccessWithData(XmlUtil.mapToXmlStr(tmpMap));
    }


    /*
     * description: 数据校验
     * author: jiangyf
     * date: 2023/4/2 9:40
     * @param json
     * @return util.SelfResponse
     */
    public SelfResponse verifyData(Map<String, Object> json) {
        String tyep = "";
        String value = "";
        // 为空、
        verifyInput(tyep, value);
        return SelfResponse.createBySuccess();
    }

    /*
     * description: BigDecimal示例
     * author: jiangyf
     * date: 2023/4/2 13:20
     * @param
     * @return util.SelfResponse
     */
    public SelfResponse decimalCal() {
        // TODO valueOf >= ZERO >>new. 初始化时优先使用valueof(),避免使用new BigDecimal进行初始化
        double num = 0.2;
        BigDecimal bigNum = BigDecimal.valueOf(num);
        Map<String, Object> rtnMap = new TreeMap<>();
        // 1-BigDecimal初始化 -ZERO
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 300000; i++) {
            BigDecimal bigZero = BigDecimal.ZERO;
        }
        long time2 = System.currentTimeMillis();
        long zeroMs = time2 - time1;
        rtnMap.put("1-ZERO耗时", zeroMs);

        // 2-BigDecimal初始化 - new
        long time3 = System.currentTimeMillis();
        for (int i = 0; i < 300000; i++) {
            BigDecimal bigZero = new BigDecimal(0);
        }
        long time4 = System.currentTimeMillis();
        long newMs = time4 - time3;
        rtnMap.put("2-new耗时", newMs);

        // 3-BigDecimal初始化 - valueOf
        long time5 = System.currentTimeMillis();
        for (int i = 0; i < 300000; i++) {
            BigDecimal bigZero = BigDecimal.valueOf(0);
        }
        long time6 = System.currentTimeMillis();
        long valueofMs = time6 - time5;
        rtnMap.put("3-valueOf耗时", valueofMs);
        rtnMap.put("4-ZERO与valueof耗时比例", zeroMs / valueofMs);
        rtnMap.put("5-new与valueof耗时比例", newMs / valueofMs);
        return SelfResponse.createBySuccessWithData(rtnMap);
    }


    /*
     * description: 生成list数据
     * author: jiangyf
     * date: 2023/4/1 14:03
     * @param number
     * @return java.util.List<com.inspur.test.demo.DemoEntity>
     */
    public List<DemoEntity> generateList(int number) {
        List<DemoEntity> demoList = new ArrayList<>();
        for (int count = 0; count <= number; count++) {
            DemoEntity demoEntity = new DemoEntity();
            demoEntity.setId(UUID.randomUUID().toString());
            demoEntity.setCode(String.valueOf(count));
            demoEntity.setName("testDemo");
            demoEntity.setNote("this is a note,put your msg");
            demoList.add(demoEntity);
        }
        return demoList;
    }


    public List<DemoEntity> generateList4Ahadle() {
        List<DemoEntity> deptList = new ArrayList<>();
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("101");
        demoEntity.setName("生产成本开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("102");
        demoEntity.setName("智能工厂开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("103");
        demoEntity.setName("项目资产开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("104");
        demoEntity.setName("供应链开发组");
        demoEntity.setNote("智慧管控事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("105");
        demoEntity.setName("财务开发组");
        demoEntity.setNote("智慧管控事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("106");
        demoEntity.setName("数据与集成组");
        demoEntity.setNote("智慧管控事业部");
        deptList.add(demoEntity);


        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("100");
        demoEntity.setName("装备制造事业部");
        demoEntity.setNote("战略大客户事业部");
        deptList.add(demoEntity);

        return deptList;
    }

    public List<DemoEntity> generateList4Bhadle() {
        List<DemoEntity> deptList = new ArrayList<>();
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("101");
        demoEntity.setName("生产成本开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("102");
        demoEntity.setName("智能工厂开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("103");
        demoEntity.setName("项目资产开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("104");
        demoEntity.setName("供应链开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("105");
        demoEntity.setName("财务开发组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("106");
        demoEntity.setName("数据与集成组");
        demoEntity.setNote("装备制造事业部");
        deptList.add(demoEntity);

        demoEntity.setId(UUID.randomUUID().toString());
        demoEntity.setCode("100");
        demoEntity.setName("装备制造事业部");
        demoEntity.setNote("战略大客户事业部");
        deptList.add(demoEntity);

        return deptList;
    }

    @Override
    public String verifyInput(String type, String value) {
        return null;
    }
}
