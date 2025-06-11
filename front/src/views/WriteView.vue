<script setup lang="ts">
import {ref} from "vue";

import axios from 'axios';
import {useRouter} from "vue-router";

const title = ref("");
const content = ref("");

const router = useRouter();

const write = function () {
  //alert(title + " / " + content)
  console.log(title.value, content.value)

  axios.post("/api/posts", {
    title: title.value,
    content: content.value
  }).then(() => {
    router.replace({name: "home"})
  });
};


</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input size="large" placeholder="제목을 입력해주세요"/>
    </el-form-item>

    <el-form-item label="내용">
      <el-input type="textarea" rows="15" placeholder="내용을 입력해주세요"/>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" style="width: 100%">등록 완료</el-button>
    </el-form-item>

  </el-form>
</template>

<style>

</style>