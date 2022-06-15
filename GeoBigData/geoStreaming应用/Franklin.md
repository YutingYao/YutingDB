
# 1. `franklin` (Spatiotemporal Asset Catalog (STAC))

## 1.1. What's `franklin`?

A [STAC](https://github.com/radiantearth/stac-spec) and [OGC API Features](http://docs.opengeospatial.org/is/17-069r3/17-069r3.html) 专注于终端用户易用性的合规网络服务.

`franklin` 导入并提供 STAC 目录，将数据存储在 Postgres 中。其目标是使 STAC 目录的导入和查询尽可能简单

## 1.2. How do I get it?

`franklin` publishes [docker](https://quay.io/repository/azavea/franklin?tab=tags) 公开可用的容器，可以部署在可以部署 docker 的任何地方。要运行容器，您需要提供一个 postgres 数据库供 `franklin` 连接。

## 1.3. Running `franklin`

`franklin` 由三个命令组成：
 - `import` 获取现有目录并将其添加到数据库中
 - `migrate`运行迁移以设置数据库
 - `serve` 启动 API 服务器

### 1.3.1. Database configuration

默认情况下，`franklin` 尝试连接到名为 `franklin` 的数据库，位于 `localhost:5432`，密码为 `franklin`。使用以下 CLI 选项，您可以自定义数据库连接*：

```bash
    --db-user <string>
        User to connect with database with
    --db-password <string>
        Database password to use
    --db-host <string>
        Database host to connect to
    --db-port <integer>
        Port to connect to database on
    --db-name <string>
        Database name to connect to
```

**也可以使用环境变量设置数据库连接选项: `DB_USER`, `DB_PASSWORD`, `DB_HOST`, `DB_PORT`, `DB_NAME`*

### 1.3.2. 示例命令

以下示例展示了如何通过导入本地目录开始使用 `franklin`。

#### 1.3.2.1. 启动数据库

第一步是找到要连接的数据库。如果您已经有安装了 PostGIS 的 Postgres 实例，则可以跳过此步骤。如果您没有数据库，请不要担心 -使用 docker 很容易上手。

Azavea 维护 [images](https://quay.io/repository/azavea/postgis?tab=tags) 安装了 PostGIS，便于开发使用。在这种情况下，我们将启动一个安装了 Postgres 11 和 PostGIS 2.5 的容器。

```bash
docker run -d -p 5432:5432 \
  --name franklin-database \
  -e POSTGRES_PASSWORD=franklinsecret \
  -e POSTGRES_USER=benjamin \
  -e POSTGRES_DB=franklin \
  quay.io/azavea/postgis:3-postgres12.2-slim
```

这会在后台启动一个数据库（`franklin-database`）。如果您想查看它是否仍在运行，您可以使用 `docker ps` 命令。如果你想在某个时候停止它，你可以使用命令`docker stop franklin-database`，这将停止容器。

#### 1.3.2.2. 运行迁移

启动数据库后，您需要对其运行迁移，以便我们可以导入数据并启动 Web 服务。迁移是一种使用表、扩展和索引配置数据库的方法，以确保 Web 服务和导入程序可以正常运行。运行迁移通常是您希望升级 `franklin` 时所涉及的第一步。要运行迁移，请使用以下命令：

```bash
docker run \
  --link franklin-database:franklin-database \
  quay.io/azavea/franklin:latest \
  migrate \
  --db-user benjamin \
  --db-name franklin \
  --db-password franklinsecret \
  --db-host franklin-database
```

在这个命令之后，数据库应该有一些额外的表来存储地理空间数据.

#### 1.3.2.3. 导入数据

Racecars need fuel to run and `franklin` needs data to be really useful.`import` 命令允许您将任何静态 STAC 集合或目录导入到 `franklin` 中以进行动态服务。以下命令需要根据您要导入的数据所在的位置进行调整。此特定示例假定已准备好导入本地目录。

```bash
docker run \
  --link franklin-database:franklin-database \
  -v $HOME/projects/franklin/data/:/opt/data/ \
  quay.io/azavea/franklin:latest \
  import-catalog \
  --db-user benjamin \
  --db-name franklin \
  --db-password franklinsecret \
  --db-host franklin-database \
  --catalog-root /opt/data/catalog.json
```

#### 1.3.2.4. 运行服务

此时，您已准备好运行该服务。要在默认端口“9090”上启动服务，您可以使用“docker run”运行“serve”命令：

```bash
docker run \
  --link franklin-database:franklin-database \
  -p 9090:9090 \
  quay.io/azavea/franklin:latest \
  serve \
  --db-user benjamin \
  --db-name franklin \
  --db-password franklinsecret \
  --db-host franklin-database
```

通过将选项传递给 `serve` 命令，还可以通过 `franklin` CLI*配置其他 API 选项和功能：
```
  --external-port 
      端口用户/客户端请求命中
  --internal-port 
      端口服务器侦听，当服务在代理后面启动时，这将与“external-port”不同
  --api-host 
      主机名富兰克林托管它 (e.g. localhost)
  --api-path 
      根的路径组件 Franklin instance (e.g. /stac/api)
  --api-scheme 
      Scheme服务器暴露给最终用户
  --default-limit 
      分页响应中返回的项目的默认限制
  --with-transactions
      是否响应事务请求，例如添加或更新项目
  --with-tiles
      是否包含 tile endpoints
```

**API 选项也可以使用环境变量设置： `API_EXTERNAL_PORT`, `API_INTERNAL_PORT`, `API_HOST`, `API_PATH`, `API_SCHEME`, `API_DEFAULT_LIMIT`, `API_WITH_TRANSACTIONS`, `API_WITH_TILES`*


### 1.3.3. 使用 docker compose

上面的示例说明了通过 `docker run` 命令运行 `franklin` 时可以使用的单独命令和一些选项。但是，如果您熟悉 `docker-compose`，可以使用以下配置快速入门。

首先，将此文件本地复制到 `docker-compose.yml` 文件中：

```yaml
version: '2.3'
services:
  database:
    image: quay.io/azavea/postgis:2.3-postgres9.6-slim
    environment:
      - POSTGRES_USER=franklin
      - POSTGRES_PASSWORD=franklin
      - POSTGRES_DB=franklin
    healthcheck:
      test: ["CMD", "pg_isready", "-U", "franklin"]
      interval: 3s
      timeout: 3s
      retries: 3
      start_period: 5s

  franklin:
    image: quay.io/azavea/franklin:latest
    depends_on:
      database:
        condition: service_healthy
	command:
	  - serve
    volumes:
      - ./:/opt/franklin/
    environment:
      - ENVIRONMENT=development
      - DB_HOST=database.service.internal
      - DB_NAME=franklin
      - DB_USER=franklin
      - DB_PASSWORD=franklin
      - AWS_PROFILE
      - AWS_REGION
    links:
      - database:database.service.internal
    ports:
      - "9090:9090"
```

其次，您可以运行`docker-compose run franklin migrate`来设置数据库。接下来，您可以通过将本地数据集复制到与您创建的 `docker-compose.yml` 文件相同的目录来导入本地数据集。假设根目录是 `catalog.json`，命令将是 `docker-compose run franklin import-catalog --catalog-root /opt/franklin/catalog.json`。最后，一旦导入完成，您就可以启动网络服务器并转到 [`localhost:9090`](http://localhost:9090) 查看您的目录。