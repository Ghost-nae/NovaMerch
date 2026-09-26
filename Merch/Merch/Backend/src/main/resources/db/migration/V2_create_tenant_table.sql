CREATE TABLE Tenants (
  id UUID PRIMARY KEY,

  name VARCHAR(150) NOT NULL,
  code VARCHAR(50) NOT NULL,
  status VARCHAR(30) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

  CONSTRAINT uk_tenant_code UNIQUE (code),

  CONSTRAINT chk_tenant_status CHECK(
    status IN(
      'PENDING',
      'ACTIVE',
      'SUSPENDED',
      'DEACTIVATED'
    )
  )
);
