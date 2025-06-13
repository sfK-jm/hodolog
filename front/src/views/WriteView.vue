<script setup lang="ts">

import {reactive} from "vue";
import PostWrite from "@/entity/post/PostWrite";
import {container} from "tsyringe";
import PostRepository from "@/repository/PostRepository";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import type HttpError from "@/http/HttpError";

const state = reactive({
  postWrite: new PostWrite(),
})

const POST_REPOSITORY = container.resolve(PostRepository);
const router = useRouter();

function write() {
  POST_REPOSITORY.write(state.postWrite)
      .then(() => {
        ElMessage({type: "success", message: "글 등록이 완료되었습니다. :)"});
        router.replace("/");
      })
      .catch((e: HttpError) => {
        ElMessage({type: "error", message: e.message});
      });
}

</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input v-model="state.postWrite.title" size="large" placeholder="제목을 입력해주세요"/>
    </el-form-item>

    <el-form-item label="내용">
      <el-input v-model="state.postWrite.content" type="textarea" rows="15" placeholder="내용을 입력해주세요"/>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" style="width: 100%" @click="write()">등록 완료</el-button>
    </el-form-item>

  </el-form>
</template>

<style>

</style>