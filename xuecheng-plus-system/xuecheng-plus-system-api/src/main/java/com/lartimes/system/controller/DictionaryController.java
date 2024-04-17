package com.lartimes.system.controller;

import com.lartimes.system.model.po.Dictionary;
import com.lartimes.system.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
@Tag(name = "DictionaryController", description = "字典controller")
public class DictionaryController  {

    @Autowired
    private DictionaryService  dictionaryService;

    @Operation(summary = "queryAll数据字典" ,description = "查询所有数据字典")
    @GetMapping("/dictionary/all")
    public List<Dictionary> queryAll() {
        return dictionaryService.queryAll();
    }

    @Operation(summary = "getByCode" ,description = "根据code获取某个字典")
    @GetMapping("/dictionary/code/{code}")
    public Dictionary getByCode(@PathVariable String code) {
        return dictionaryService.getByCode(code);
    }
}
