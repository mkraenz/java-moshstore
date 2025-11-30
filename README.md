# Moshstore

Based on [Course by Mosh Hamedani](https://members.codewithmosh.com/courses/spring-boot-mastering-apis), and then
extended.

## Deployment

Push to
GitHub. [Railway](https://railway.com/project/944271c2-6983-4df6-b688-d0022a8c2656/service/5c4e575a-0408-4621-ba5a-b41b06d73d0b?environmentId=e42e3b41-d1bd-4b45-a581-b3e457776137)
automatically picks up changes and redeploys the app.

### Local build

Package the app

```sh
./mvnw clean package
```

Theoretically, if all env vars would be set, you can start the build locally using:

```sh
java -jar ./target/moshstore-1.0.0.jar
```

## Development

### Stripe

```sh
 STRIPE_API_KEY='CHANGE_THIS'

alias stripe="docker run -it --env STRIPE_API_KEY=\"$STRIPE_API_KEY\" stripe/stripe-cli:latest"

stripe listen --forward-to http://localhost:8080/checkout/webhooks/stripe
# since we're running stripe from inside a docker container, we have to forward to the container host which is available at 172.17.0.1, port 8080 is our backend server
stripe listen --forward-to http://172.17.0.1:8080/checkout/webhooks/stripe

stripe products create --name="My First Product" --description="Created with the Stripe CLI"
stripe products delete prod_TUzkL2BXBnuhbD

# while we have stripe listening in another terminal, in a second terminal we can now trigger test events
stripe trigger payment_intent.succeeded
stripe trigger payment_intent.succeeded --add "payment_intent:metadata[order_id]=8"
```
