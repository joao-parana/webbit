CREATE TABLE RELATORIO (codigo BIGINT NOT NULL, DATA_REGISTRO DATETIME, NOME_CUBO VARCHAR(255) NOT NULL, DESCRICAO VARCHAR(255) NOT NULL, MDX_QUERY VARCHAR(255), NOME VARCHAR(255) NOT NULL, LOGIN_USUARIO VARCHAR(255), PRIMARY KEY (codigo))
CREATE TABLE HISTORICO_VIDA_UTIL (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, VIDA_HORAS FLOAT, VIDA_PERCENTUAL FLOAT, METODO INTEGER, OMEGA FLOAT, DATA_MEDIDA BIGINT, COD_FONTE_DADO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE ALARME (CODIGO BIGINT NOT NULL, DADOS_ALARMES VARCHAR(2048) NOT NULL, DATA_ALARME BIGINT NOT NULL, DATA_REGISTRO DATETIME, TEXTO_INTERVENCAO VARCHAR(255), DATA_INTERVENCAO BIGINT, REMOTE_ALARME_CODE BIGINT, SUPERVISOR VARCHAR(255), COD_FONTE_DADO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE COMPORTAMENTO (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TIPO INTEGER NOT NULL, TITULO VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE COMPONENTE (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), ITEM_CODIGO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE META_DADOS (CODIGO BIGINT NOT NULL, TIPO_CLASSE VARCHAR(31), DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), TIPO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE FONTE_DADO (CODIGO BIGINT NOT NULL, indice INTEGER, STATUS CHAR(1) NOT NULL, COD_COMPONENTE BIGINT NOT NULL, LOCALIZACAO BIGINT NOT NULL, FILA BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE HISTORICO (CODIGO BIGINT NOT NULL, MEDIA FLOAT, DATA_REGISTRO DATETIME, MAXIMO FLOAT, MINIMO FLOAT, RMS FLOAT, DATA_MEDIDA BIGINT, INTERVALO_GRAVACAO INTEGER, DESVIO_PADRAO FLOAT, COD_FONTE_DADO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE UNIDADE_GERADORA (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), COD_USINA BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE LOCALIZACAO (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE MEDIDA_ADQUIRIDA_BRUTA (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DATA_PRIMEIRA_MEDIDA BIGINT NOT NULL, NUM_ELEMENTOS INTEGER NOT NULL, COD_FONTE_DADO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE USINA (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE FILA (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE ARQUIVO_DADOS_BRUTOS (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, ID_ARQUIVO BIGINT NOT NULL, COD_FONTE_DADO BIGINT NOT NULL UNIQUE, PRIMARY KEY (CODIGO))
CREATE TABLE INDICE_DADOS_BRUTOS (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, TIMESTAMP_INICIAL BIGINT NOT NULL, TIMESTAMP_FINAL BIGINT NOT NULL, COD_FONTE_DADO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE MEDICOES_VALIDACAO_SADM (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, NOME_FONTE_DADO VARCHAR(255) NOT NULL, DATA_MEDIDA BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE AGENDAMENTO_USUARIO (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, POSICAO_FINAL BIGINT NOT NULL, POSICAO_INICIAL BIGINT NOT NULL, USUARIO VARCHAR(255) NOT NULL, CODIGO_MAB BIGINT NOT NULL, CODIGO_REQUISICAO BIGINT, PRIMARY KEY (CODIGO))
CREATE TABLE SOLICITACAO_AGENDAMENTO (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, FONTES_DADO VARCHAR(255) NOT NULL, DESCRICAO VARCHAR(255), DURACAO BIGINT NOT NULL, TIMESTAMP_FINAL BIGINT NOT NULL, TIMESTAMP_INICIAL BIGINT NOT NULL, NOME VARCHAR(255) NOT NULL, PERIODO_REPETICAO TINYINT NOT NULL, TIPO_REPETICAO TINYINT NOT NULL, STATUS TINYINT NOT NULL, TITULO VARCHAR(255), USUARIO VARCHAR(255) NOT NULL, COD_ALARME BIGINT, PRIMARY KEY (CODIGO))
CREATE TABLE VIEW_TREE (CODIGO BIGINT NOT NULL, COMPONENTE VARCHAR(255) NOT NULL, NOME_FONTE VARCHAR(255) NOT NULL, CODIGO_FONTE BIGINT NOT NULL, TIPO_FONTE VARCHAR(255) NOT NULL, COD_UG BIGINT NOT NULL, UG VARCHAR(255) NOT NULL, COD_USINA BIGINT NOT NULL, USINA VARCHAR(255) NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE COMPORTAMENTO_META (CODIGO BIGINT NOT NULL, PAPEL INTEGER NOT NULL, COD_COMPORTAMENTO BIGINT NOT NULL, COD_FONTE_META BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE HISTOGRAMA_VENTO (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), HISTOGRAMA LONGBLOB, NOME VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE MODELO_CONDUTOR (CODIGO BIGINT NOT NULL, A DOUBLE, B DOUBLE, DATA_REGISTRO DATETIME, DIAMETRO DOUBLE, K DOUBLE, DENSIDADE_LINEAR DOUBLE, NOME VARCHAR(255), CARGA_RUPTURA DOUBLE, PRIMARY KEY (CODIGO))
CREATE TABLE STOCKBRIDGE (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DIST_C_DE_MASSA_CONTRAP_MAIOR DOUBLE, DIST_C_DE_MASSA_CONTRAP_MENOR DOUBLE, RIGIDEZ_FLEXAO DOUBLE, FATOR_AMORTECIMENTO DOUBLE, MOM_INERCIA_CONTRAP_MAIOR DOUBLE, MOM_INERCIA_CONTRAP_MENOR DOUBLE, COMPR_CABO_MENSAGEIRO_MAIOR DOUBLE, COMPR_CABO_MENSAGEIRO_MENOR DOUBLE, MASSA_CONTRAPESO_MAIOR DOUBLE, MASSA_CONTRAPESO_MENOR DOUBLE, FABRICANTE VARCHAR(255), MASSA_GARRA DOUBLE, MODELO VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE STOCKBRIDGE_EXP (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), FABRICANTE VARCHAR(255), MODELO VARCHAR(255), DADOS_AMORTECEDOR LONGBLOB, PRIMARY KEY (CODIGO))
CREATE TABLE TIPO_FONTE_DADO (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), COD_GRANDEZA BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE GRANDEZA (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), GRUPO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), UNIDADE_MEDIDA VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE USUARIO (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), EMAIL VARCHAR(255), NOME VARCHAR(255) NOT NULL, SENHA VARCHAR(255) NOT NULL, TITULO VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE GRUPO (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE APLICACAO (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, TITULO VARCHAR(255), PRIMARY KEY (CODIGO))
CREATE TABLE FUNCIONALIDADE (CODIGO BIGINT NOT NULL, PARAMETROS_CONFIGURACAO VARCHAR(255) NOT NULL, DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), CLASSE_INTERFACE VARCHAR(255) NOT NULL, NOME VARCHAR(255) NOT NULL, PRIORIDADE INTEGER NOT NULL, TITULO VARCHAR(255), COD_APLICACAO BIGINT, PRIMARY KEY (CODIGO))
CREATE TABLE TEM_OCORRENCIA (CODIGO BIGINT NOT NULL, OCORRENCIAS INTEGER NOT NULL, DATA_REGISTRO DATETIME, DIA INTEGER NOT NULL, MES INTEGER NOT NULL, CODIGO_PLANTA BIGINT NOT NULL, USUARIO VARCHAR(255) NOT NULL, ANO INTEGER NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE DETALHE_OCORRENCIA (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, COD_FONTE_DADO BIGINT NOT NULL, CODIGO_REQUISICAO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE VIDA_RESIDUAL_MEDIDA (DATA BIGINT NOT NULL, VALOR FLOAT NOT NULL, COD_SERIES BIGINT NOT NULL, PRIMARY KEY (DATA, COD_SERIES))
CREATE INDEX SOMA_RL_META_TIME_INDEX ON VIDA_RESIDUAL_MEDIDA (COD_SERIES, DATA)
CREATE TABLE VIDA_RESIDUAL_SERIES (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, INTERVALO FLOAT NOT NULL, COD_VIDA_META BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE VIDA_RESIDUAL_META (CODIGO BIGINT NOT NULL, METODO_CALCULO VARCHAR(255) NOT NULL, TIPO_DEGRADACAO VARCHAR(255) NOT NULL, SEGMENTO VARCHAR(255) NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE ABS_ARVORE_FOLHA (CODIGO BIGINT NOT NULL, TIPO_CLASSE VARCHAR(31), DATA_REGISTRO DATETIME, DESCRICAO VARCHAR(255), NOME VARCHAR(255) NOT NULL, REFERENCIA BIGINT NOT NULL, TITULO VARCHAR(255) NOT NULL, TIPO VARCHAR(255) NOT NULL, PAI BIGINT, PRIMARY KEY (CODIGO))
CREATE TABLE ABS_ARVORE_NO (CODIGO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE ARVORE_FOLHA (CODIGO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE ARVORE_NO (CODIGO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE HISTORICO_META (CODIGO BIGINT NOT NULL, ETIQUETA VARCHAR(255) NOT NULL, COD_FONTE_DADO BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE HISTORICO_SERIES (CODIGO BIGINT NOT NULL, DATA_REGISTRO DATETIME, INTERVALO FLOAT NOT NULL, COD_HIST_META BIGINT NOT NULL, PRIMARY KEY (CODIGO))
CREATE TABLE HISTORICO_MEDIDA (DATA BIGINT NOT NULL, VALOR FLOAT NOT NULL, COD_HIST_SERIES BIGINT NOT NULL, PRIMARY KEY (DATA, COD_HIST_SERIES))
CREATE INDEX SOMA_HIST_SERIES_TIME_INDEX ON HISTORICO_MEDIDA (COD_HIST_SERIES, DATA)
CREATE TABLE GRUPO_USUARIO (COD_USUARIO BIGINT NOT NULL, COD_GRUPO BIGINT NOT NULL, PRIMARY KEY (COD_USUARIO, COD_GRUPO))
CREATE TABLE PERMISSAO_APLICACAO (COD_GRUPO BIGINT NOT NULL, COD_APLICACAO BIGINT NOT NULL, PRIMARY KEY (COD_GRUPO, COD_APLICACAO))
CREATE TABLE PERMISSAO_FUNCIONALIDADE (COD_GRUPO BIGINT NOT NULL, COD_FUNCIONALIDADE BIGINT NOT NULL, PRIMARY KEY (COD_GRUPO, COD_FUNCIONALIDADE))
ALTER TABLE COMPORTAMENTO ADD CONSTRAINT UNQ_COMPORTAMENTO_0 UNIQUE (NOME)
ALTER TABLE COMPONENTE ADD CONSTRAINT UNQ_COMPONENTE_0 UNIQUE (NOME)
ALTER TABLE META_DADOS ADD CONSTRAINT UNQ_META_DADOS_0 UNIQUE (NOME)
ALTER TABLE UNIDADE_GERADORA ADD CONSTRAINT UNQ_UNIDADE_GERADORA_0 UNIQUE (NOME)
ALTER TABLE LOCALIZACAO ADD CONSTRAINT UNQ_LOCALIZACAO_0 UNIQUE (NOME)
ALTER TABLE USINA ADD CONSTRAINT UNQ_USINA_0 UNIQUE (NOME)
ALTER TABLE FILA ADD CONSTRAINT UNQ_FILA_0 UNIQUE (NOME)
ALTER TABLE SOLICITACAO_AGENDAMENTO ADD CONSTRAINT UNQ_SOLICITACAO_AGENDAMENTO_0 UNIQUE (NOME)
ALTER TABLE TIPO_FONTE_DADO ADD CONSTRAINT UNQ_TIPO_FONTE_DADO_0 UNIQUE (NOME)
ALTER TABLE GRANDEZA ADD CONSTRAINT UNQ_GRANDEZA_0 UNIQUE (NOME)
ALTER TABLE USUARIO ADD CONSTRAINT UNQ_USUARIO_0 UNIQUE (NOME)
ALTER TABLE APLICACAO ADD CONSTRAINT UNQ_APLICACAO_0 UNIQUE (NOME)
ALTER TABLE FUNCIONALIDADE ADD CONSTRAINT UNQ_FUNCIONALIDADE_0 UNIQUE (NOME)
ALTER TABLE HISTORICO_VIDA_UTIL ADD CONSTRAINT FK_HISTORICO_VIDA_UTIL_COD_FONTE_DADO FOREIGN KEY (COD_FONTE_DADO) REFERENCES FONTE_DADO (CODIGO)
ALTER TABLE ALARME ADD CONSTRAINT FK_ALARME_COD_FONTE_DADO FOREIGN KEY (COD_FONTE_DADO) REFERENCES FONTE_DADO (CODIGO)
ALTER TABLE COMPONENTE ADD CONSTRAINT FK_COMPONENTE_ITEM_CODIGO FOREIGN KEY (ITEM_CODIGO) REFERENCES UNIDADE_GERADORA (CODIGO)
ALTER TABLE META_DADOS ADD CONSTRAINT FK_META_DADOS_TIPO FOREIGN KEY (TIPO) REFERENCES TIPO_FONTE_DADO (CODIGO)
ALTER TABLE FONTE_DADO ADD CONSTRAINT FK_FONTE_DADO_FILA FOREIGN KEY (FILA) REFERENCES FILA (CODIGO)
ALTER TABLE FONTE_DADO ADD CONSTRAINT FK_FONTE_DADO_COD_COMPONENTE FOREIGN KEY (COD_COMPONENTE) REFERENCES COMPONENTE (CODIGO)
ALTER TABLE FONTE_DADO ADD CONSTRAINT FK_FONTE_DADO_CODIGO FOREIGN KEY (CODIGO) REFERENCES META_DADOS (CODIGO)
ALTER TABLE FONTE_DADO ADD CONSTRAINT FK_FONTE_DADO_LOCALIZACAO FOREIGN KEY (LOCALIZACAO) REFERENCES LOCALIZACAO (CODIGO)
ALTER TABLE HISTORICO ADD CONSTRAINT FK_HISTORICO_COD_FONTE_DADO FOREIGN KEY (COD_FONTE_DADO) REFERENCES FONTE_DADO (CODIGO)
ALTER TABLE UNIDADE_GERADORA ADD CONSTRAINT FK_UNIDADE_GERADORA_COD_USINA FOREIGN KEY (COD_USINA) REFERENCES USINA (CODIGO)
ALTER TABLE MEDIDA_ADQUIRIDA_BRUTA ADD CONSTRAINT FK_MEDIDA_ADQUIRIDA_BRUTA_COD_FONTE_DADO FOREIGN KEY (COD_FONTE_DADO) REFERENCES FONTE_DADO (CODIGO)
ALTER TABLE ARQUIVO_DADOS_BRUTOS ADD CONSTRAINT FK_ARQUIVO_DADOS_BRUTOS_COD_FONTE_DADO FOREIGN KEY (COD_FONTE_DADO) REFERENCES FONTE_DADO (CODIGO)
ALTER TABLE INDICE_DADOS_BRUTOS ADD CONSTRAINT FK_INDICE_DADOS_BRUTOS_COD_FONTE_DADO FOREIGN KEY (COD_FONTE_DADO) REFERENCES FONTE_DADO (CODIGO)
ALTER TABLE AGENDAMENTO_USUARIO ADD CONSTRAINT FK_AGENDAMENTO_USUARIO_CODIGO_REQUISICAO FOREIGN KEY (CODIGO_REQUISICAO) REFERENCES SOLICITACAO_AGENDAMENTO (CODIGO)
ALTER TABLE AGENDAMENTO_USUARIO ADD CONSTRAINT FK_AGENDAMENTO_USUARIO_CODIGO_MAB FOREIGN KEY (CODIGO_MAB) REFERENCES MEDIDA_ADQUIRIDA_BRUTA (CODIGO)
ALTER TABLE SOLICITACAO_AGENDAMENTO ADD CONSTRAINT FK_SOLICITACAO_AGENDAMENTO_COD_ALARME FOREIGN KEY (COD_ALARME) REFERENCES ALARME (CODIGO)
ALTER TABLE COMPORTAMENTO_META ADD CONSTRAINT FK_COMPORTAMENTO_META_COD_COMPORTAMENTO FOREIGN KEY (COD_COMPORTAMENTO) REFERENCES COMPORTAMENTO (CODIGO)
ALTER TABLE COMPORTAMENTO_META ADD CONSTRAINT FK_COMPORTAMENTO_META_COD_FONTE_META FOREIGN KEY (COD_FONTE_META) REFERENCES META_DADOS (CODIGO)
ALTER TABLE TIPO_FONTE_DADO ADD CONSTRAINT FK_TIPO_FONTE_DADO_COD_GRANDEZA FOREIGN KEY (COD_GRANDEZA) REFERENCES GRANDEZA (CODIGO)
ALTER TABLE FUNCIONALIDADE ADD CONSTRAINT FK_FUNCIONALIDADE_COD_APLICACAO FOREIGN KEY (COD_APLICACAO) REFERENCES APLICACAO (CODIGO)
ALTER TABLE VIDA_RESIDUAL_MEDIDA ADD CONSTRAINT FK_VIDA_RESIDUAL_MEDIDA_COD_SERIES FOREIGN KEY (COD_SERIES) REFERENCES VIDA_RESIDUAL_SERIES (CODIGO)
ALTER TABLE VIDA_RESIDUAL_SERIES ADD CONSTRAINT FK_VIDA_RESIDUAL_SERIES_COD_VIDA_META FOREIGN KEY (COD_VIDA_META) REFERENCES VIDA_RESIDUAL_META (CODIGO)
ALTER TABLE VIDA_RESIDUAL_META ADD CONSTRAINT FK_VIDA_RESIDUAL_META_CODIGO FOREIGN KEY (CODIGO) REFERENCES META_DADOS (CODIGO)
ALTER TABLE ABS_ARVORE_FOLHA ADD CONSTRAINT FK_ABS_ARVORE_FOLHA_PAI FOREIGN KEY (PAI) REFERENCES ABS_ARVORE_FOLHA (CODIGO)
ALTER TABLE ABS_ARVORE_NO ADD CONSTRAINT FK_ABS_ARVORE_NO_CODIGO FOREIGN KEY (CODIGO) REFERENCES ABS_ARVORE_FOLHA (CODIGO)
ALTER TABLE ARVORE_FOLHA ADD CONSTRAINT FK_ARVORE_FOLHA_CODIGO FOREIGN KEY (CODIGO) REFERENCES ABS_ARVORE_FOLHA (CODIGO)
ALTER TABLE ARVORE_NO ADD CONSTRAINT FK_ARVORE_NO_CODIGO FOREIGN KEY (CODIGO) REFERENCES ABS_ARVORE_FOLHA (CODIGO)
ALTER TABLE HISTORICO_META ADD CONSTRAINT FK_HISTORICO_META_CODIGO FOREIGN KEY (CODIGO) REFERENCES META_DADOS (CODIGO)
ALTER TABLE HISTORICO_META ADD CONSTRAINT FK_HISTORICO_META_COD_FONTE_DADO FOREIGN KEY (COD_FONTE_DADO) REFERENCES FONTE_DADO (CODIGO)
ALTER TABLE HISTORICO_SERIES ADD CONSTRAINT FK_HISTORICO_SERIES_COD_HIST_META FOREIGN KEY (COD_HIST_META) REFERENCES META_DADOS (CODIGO)
ALTER TABLE HISTORICO_MEDIDA ADD CONSTRAINT FK_HISTORICO_MEDIDA_COD_HIST_SERIES FOREIGN KEY (COD_HIST_SERIES) REFERENCES HISTORICO_SERIES (CODIGO)
ALTER TABLE GRUPO_USUARIO ADD CONSTRAINT FK_GRUPO_USUARIO_COD_GRUPO FOREIGN KEY (COD_GRUPO) REFERENCES GRUPO (CODIGO)
ALTER TABLE GRUPO_USUARIO ADD CONSTRAINT FK_GRUPO_USUARIO_COD_USUARIO FOREIGN KEY (COD_USUARIO) REFERENCES USUARIO (CODIGO)
ALTER TABLE PERMISSAO_APLICACAO ADD CONSTRAINT FK_PERMISSAO_APLICACAO_COD_APLICACAO FOREIGN KEY (COD_APLICACAO) REFERENCES APLICACAO (CODIGO)
ALTER TABLE PERMISSAO_APLICACAO ADD CONSTRAINT FK_PERMISSAO_APLICACAO_COD_GRUPO FOREIGN KEY (COD_GRUPO) REFERENCES GRUPO (CODIGO)
ALTER TABLE PERMISSAO_FUNCIONALIDADE ADD CONSTRAINT FK_PERMISSAO_FUNCIONALIDADE_COD_FUNCIONALIDADE FOREIGN KEY (COD_FUNCIONALIDADE) REFERENCES FUNCIONALIDADE (CODIGO)
ALTER TABLE PERMISSAO_FUNCIONALIDADE ADD CONSTRAINT FK_PERMISSAO_FUNCIONALIDADE_COD_GRUPO FOREIGN KEY (COD_GRUPO) REFERENCES GRUPO (CODIGO)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT DECIMAL(38), PRIMARY KEY (SEQ_NAME))
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT DECIMAL(38), PRIMARY KEY (SEQ_NAME))
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)
