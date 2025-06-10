<script setup lang="ts">
import { reactive } from "vue";
import Login from "@/entity/user/Login";

import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import AxiosHttpClient from "@/http/AxiosHttpClient";
import type HttpError from "@/http/HttpError";
import type {AxiosResponse} from "axios";

const state = reactive({
  login: new Login(),
});

const router = useRouter();

function doLogin() {
  const httpClient = new AxiosHttpClient();

  httpClient
    .post({
      path: "/api/auth/login",
      body: state.login,
    })
    .then(() => {
      ElMessage({ type: "success", message: "환영햡니다. :)" });
      router.replace("/");
    })
    .catch((e: HttpError) => {
      ElMessage({ type: "error", message: e.getMessage()});
    });
}
</script>

<template>
  <el-row>
    <el-col :span="10" :offset="7">
      <el-form label-position="top">
        <el-form-item label="이메일">
          <el-input v-model="state.login.email"></el-input>
        </el-form-item>

        <el-form-item label="비밀번호">
          <el-input type="password" v-model="state.login.password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="doLogin()"
            >로그인</el-button
          >
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss"></style>
