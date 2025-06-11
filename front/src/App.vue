<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import Header from "@/components/Header.vue";
import Content from "@/components/Content.vue";
import Menu from "@/components/Menu.vue";
</script>

<template>

  <el-row>
    <el-col :span="22" :offset="1">
      <Header/>
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="22" :offset="1">
      <Menu/>
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="22" :offset="1"
            :sm="{span:20, offset:2}"
            :md="{span:16, offset:4}"
            :lg="{span:14, offset:5}">
      <Content/>
    </el-col>
  </el-row>
</template>

<style>
body {
  font-family: "Pretendard Variable", Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto,
  "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR",
  "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
}
</style>
