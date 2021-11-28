# Stage 1
FROM node:16-alpine3.13 as build-step
RUN mkdir -p /app
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . /app
RUN npm run build --prod

# Stage 2
FROM nginx:1.20-alpine
COPY --from=build-step /app/dist/kniffel /usr/share/nginx/html
EXPOSE 80 443
