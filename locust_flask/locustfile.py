from locust import HttpUser, task, between


class LocustUser(HttpUser):
    wait_time = between(3, 3)

    @task
    def index(self):
        self.client.get("/")
