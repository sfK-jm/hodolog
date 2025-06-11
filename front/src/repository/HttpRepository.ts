import AxiosHttpClient, {type HttpRequestConfig} from "@/http/AxiosHttpClient";

export default class HttpRepository {

    private readonly httpClient;

    constructor(httpClient: AxiosHttpClient = new AxiosHttpClient()) {
        this.httpClient = httpClient;
    }

    public async get(config: HttpRequestConfig) {
        return this.httpClient.request({...config, method: 'GET'})
    }

    public async post(config: HttpRequestConfig) {
        return this.httpClient.request({...config, method: 'POST'})
    }

    public async patch(config: HttpRequestConfig) {
        return this.httpClient.request({...config, method: 'PATCH'})
    }

    public async delete(config: HttpRequestConfig) {
        return this.httpClient.request({...config, method: 'DELETE'})
    }
};