import HttpRepository from "@/repository/HttpRepository";
import type Login from "@/entity/user/Login";
import {inject, singleton} from "tsyringe";
import type PostWrite from "@/entity/post/PostWrite";

@singleton()
export default class PostRepository{

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public write(request: PostWrite){
        return this.httpRepository.post({
            path: '/api/posts',
            body: request,
        })
    }
};