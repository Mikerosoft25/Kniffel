# Stage 1
FROM node:16-alpine as build-step
RUN mkdir -p /app
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . /app
RUN npm run build:release

# Stage 2
FROM nginx:1.23.1-alpine
COPY --from=build-step /app/dist/kniffel /usr/share/nginx/html
EXPOSE 80 443
