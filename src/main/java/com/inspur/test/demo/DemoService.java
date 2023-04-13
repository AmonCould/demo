package com.inspur.test.demo;

import cn.hutool.core.util.XmlUtil;
import com.inspur.test.demo.factory.verifyInterface;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.SelfResponse;

import java.io.StringWriter;
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

    /*
     * description: 数组的交、并、差集处理
     * author: jiangyf
     * date: 2023/4/10 15:28
     * @param
     * @return util.SelfResponse
     */
    public SelfResponse arrayListHandel() {
        // 数据组织
        List<DemoEntity> deptListOld = generateList4Ahadle();
        List<DemoEntity> deptListNew = generateList4Bhadle();


        return SelfResponse.createBySuccessWithMsg("123");

    }



    /*
     * description: 对象到Xml
     * author: jiangyf
     * date: 2023/4/2 9:40
     * @param json
     * @return util.SelfResponse
     */
    public SelfResponse xmlTransform(Map<String, Object> json)  {
        if (StringUtils.isEmpty(json.get("proName").toString())) {
            return SelfResponse.createByErrorWithMsg("实体名称为空");
        }
        try{
            Object object = Class.forName(json.get("proName").toString()).newInstance();
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
            throw new RuntimeException(e);
        }
        // 组织数据
        List<DemoEntity> demoList = generateList4Bhadle();
        XStream xstream = new XStream();
        XStream.setupDefaultSecurity(xstream);
        xstream.alias("organization", List.class);
        xstream.alias("department", DemoEntity.class);
        StringWriter writer = new StringWriter();
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        xstream.toXML(demoList, writer);
        String xmlStr = writer.toString();

        return SelfResponse.createBySuccessWithData(xmlStr);
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

        DemoEntity demoEntity1 = new DemoEntity();
        demoEntity1.setId(UUID.randomUUID().toString());
        demoEntity1.setCode("102");
        demoEntity1.setName("智能工厂开发组");
        demoEntity1.setNote("装备制造事业部");
        deptList.add(demoEntity1);

        DemoEntity demoEntity2 = new DemoEntity();
        demoEntity2.setId(UUID.randomUUID().toString());
        demoEntity2.setCode("103");
        demoEntity2.setName("项目资产开发组");
        demoEntity2.setNote("装备制造事业部");
        deptList.add(demoEntity2);

        DemoEntity demoEntity3 = new DemoEntity();
        demoEntity3.setId(UUID.randomUUID().toString());
        demoEntity3.setCode("104");
        demoEntity3.setName("供应链开发组");
        demoEntity3.setNote("智慧管控事业部");
        deptList.add(demoEntity3);

        DemoEntity demoEntity4 = new DemoEntity();
        demoEntity4.setId(UUID.randomUUID().toString());
        demoEntity4.setCode("105");
        demoEntity4.setName("财务开发组");
        demoEntity4.setNote("智慧管控事业部");
        deptList.add(demoEntity4);

        DemoEntity demoEntity5 = new DemoEntity();
        demoEntity5.setId(UUID.randomUUID().toString());
        demoEntity5.setCode("106");
        demoEntity5.setName("数据与集成组");
        demoEntity5.setNote("智慧管控事业部");
        deptList.add(demoEntity5);

        DemoEntity demoEntity6 = new DemoEntity();
        demoEntity6.setId(UUID.randomUUID().toString());
        demoEntity6.setCode("100");
        demoEntity6.setName("船舶与装备事业部");
        demoEntity6.setNote("战略大客户事业部");
        deptList.add(demoEntity6);

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

        DemoEntity demoEntity1 = new DemoEntity();
        demoEntity1.setId(UUID.randomUUID().toString());
        demoEntity1.setCode("102");
        demoEntity1.setName("智能工厂开发组");
        demoEntity1.setNote("装备制造事业部");
        deptList.add(demoEntity1);

        DemoEntity demoEntity2 = new DemoEntity();
        demoEntity2.setId(UUID.randomUUID().toString());
        demoEntity2.setCode("103");
        demoEntity2.setName("项目资产开发组");
        demoEntity2.setNote("装备制造事业部");
        deptList.add(demoEntity2);

        DemoEntity demoEntity3 = new DemoEntity();
        demoEntity3.setId(UUID.randomUUID().toString());
        demoEntity3.setCode("104");
        demoEntity3.setName("供应链开发组");
        demoEntity3.setNote("装备制造事业部");
        deptList.add(demoEntity3);

        DemoEntity demoEntity4 = new DemoEntity();
        demoEntity4.setId(UUID.randomUUID().toString());
        demoEntity4.setCode("105");
        demoEntity4.setName("财务开发组");
        demoEntity4.setNote("装备制造事业部");
        deptList.add(demoEntity4);

        DemoEntity demoEntity5 = new DemoEntity();
        demoEntity5.setId(UUID.randomUUID().toString());
        demoEntity5.setCode("106");
        demoEntity5.setName("数据与集成组");
        demoEntity5.setNote("装备制造事业部");
        deptList.add(demoEntity5);

        DemoEntity demoEntity6 = new DemoEntity();
        demoEntity6.setId(UUID.randomUUID().toString());
        demoEntity6.setCode("100");
        demoEntity6.setName("装备制造事业部");
        demoEntity6.setNote("战略大客户事业部");
        deptList.add(demoEntity6);

        return deptList;
    }

    @Override
    public String verifyInput(String type, String value) {
        return null;
    }
}
