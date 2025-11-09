-- ENUMS
CREATE TYPE movement_type AS ENUM ('IN','OUT','TRANSFER');
CREATE TYPE movement_status AS ENUM ('DRAFT','POSTED','CANCELLED');
CREATE TYPE price_list_type AS ENUM ('SALE','PURCHASE');

-- COMPANY / BRANCH
CREATE TABLE company (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(50) UNIQUE NOT NULL,
  name VARCHAR(255) NOT NULL,
  currency VARCHAR(3) NOT NULL DEFAULT 'USD',
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE branch (
  id BIGSERIAL PRIMARY KEY,
  company_id BIGINT NOT NULL REFERENCES company(id) ON DELETE CASCADE,
  code VARCHAR(50) NOT NULL,
  name VARCHAR(255) NOT NULL,
  location TEXT,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now(),
  UNIQUE(company_id, code)
);

-- UNITS
CREATE TABLE unit (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(20) UNIQUE NOT NULL,
  name VARCHAR(100) NOT NULL
);

-- SUPPLIER (simple)
CREATE TABLE supplier (
  id BIGSERIAL PRIMARY KEY,
  company_id BIGINT NOT NULL REFERENCES company(id) ON DELETE CASCADE,
  name VARCHAR(255) NOT NULL,
  contact_email VARCHAR(255),
  phone VARCHAR(50),
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now(),
  UNIQUE(company_id, name)
);

-- WAREHOUSE
CREATE TABLE warehouse (
  id BIGSERIAL PRIMARY KEY,
  company_id BIGINT NOT NULL REFERENCES company(id) ON DELETE CASCADE,
  branch_id BIGINT NOT NULL REFERENCES branch(id) ON DELETE CASCADE,
  code VARCHAR(50) NOT NULL,
  name VARCHAR(255) NOT NULL,
  location TEXT,
  allow_negative_stock BOOLEAN NOT NULL DEFAULT FALSE,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now(),
  UNIQUE(company_id, code)
);

-- PRODUCT
CREATE TABLE product (
  id BIGSERIAL PRIMARY KEY,
  company_id BIGINT NOT NULL REFERENCES company(id) ON DELETE CASCADE,
  sku VARCHAR(100) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  unit_id BIGINT NOT NULL REFERENCES unit(id),
  supplier_id BIGINT REFERENCES supplier(id) ON DELETE SET NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now(),
  UNIQUE(company_id, sku)
);

CREATE TABLE inventory (
  id BIGSERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
  warehouse_id BIGINT NOT NULL REFERENCES warehouse(id) ON DELETE CASCADE,
  quantity NUMERIC(18,4) NOT NULL DEFAULT 0,
  reserved NUMERIC(18,4) NOT NULL DEFAULT 0,
  updated_at TIMESTAMP NOT NULL DEFAULT now(),
  UNIQUE(product_id, warehouse_id),
  CHECK (quantity >= 0),
  CHECK (reserved >= 0)
);

-- MOVEMENTS
CREATE TABLE movement_document (
  id BIGSERIAL PRIMARY KEY,
  company_id BIGINT NOT NULL REFERENCES company(id) ON DELETE CASCADE,
  branch_id BIGINT NOT NULL REFERENCES branch(id) ON DELETE CASCADE,
  reference VARCHAR(255),
  status movement_status NOT NULL DEFAULT 'DRAFT',
  notes TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  posted_at TIMESTAMP,
  created_by VARCHAR(100),
  posted_by VARCHAR(100),
  CHECK ((status <> 'POSTED') OR (posted_at IS NOT NULL))
);

CREATE TABLE movement_line (
  id BIGSERIAL PRIMARY KEY,
  document_id BIGINT NOT NULL REFERENCES movement_document(id) ON DELETE CASCADE,
  type movement_type NOT NULL,
  product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE RESTRICT,
  from_warehouse_id BIGINT REFERENCES warehouse(id) ON DELETE RESTRICT,
  to_warehouse_id BIGINT REFERENCES warehouse(id) ON DELETE RESTRICT,
  unit_id BIGINT NOT NULL REFERENCES unit(id),
  quantity NUMERIC(18,4) NOT NULL CHECK (quantity > 0),
  unit_cost NUMERIC(18,4) CHECK (unit_cost >= 0),
  CHECK (
    (type = 'IN'  AND to_warehouse_id   IS NOT NULL AND from_warehouse_id IS NULL)
 OR (type = 'OUT' AND from_warehouse_id IS NOT NULL AND to_warehouse_id   IS NULL)
 OR (type = 'TRANSFER' AND from_warehouse_id IS NOT NULL AND to_warehouse_id IS NOT NULL
                        AND from_warehouse_id <> to_warehouse_id)
  )
);
