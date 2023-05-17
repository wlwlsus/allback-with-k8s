from locust import HttpUser, task, between


class LocustUser(HttpUser):
    wait_time = between(1, 1)

    @task
    def index(self):
        self.client.get("/")
