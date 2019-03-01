package com.tc.config;

import org.junit.Test;
import org.terracotta.config.BindPort;
import org.terracotta.config.Server;

import com.tc.net.TCSocketAddress;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ServerConfigurationTest {

  private static final String SERVER_NAME = "server-1";
  private static final String LOCALHOST = "localhost";
  private static final String LOGS = "logs";
  private static final int    TSA_PORT = 1000;
  private static final int    GROUP_PORT = 1100;

  @Test
  public void testConfiguration() {
    int reconnectWindow = 100;
    ServerConfiguration serverConfiguration =
        new ServerConfiguration(createServer(false), reconnectWindow);

    assertThat(serverConfiguration.getTsaPort().getBind(), is(LOCALHOST));
    assertThat(serverConfiguration.getGroupPort().getBind(), is(LOCALHOST));
    assertThat(serverConfiguration.getTsaPort().getValue(), is(TSA_PORT));
    assertThat(serverConfiguration.getGroupPort().getValue(), is(GROUP_PORT));
    assertThat(serverConfiguration.getName(), is(SERVER_NAME));
    assertThat(serverConfiguration.getHost(), is(LOCALHOST));
    assertThat(serverConfiguration.getLogsLocation(), is(new File(LOGS)));
    assertThat(serverConfiguration.getClientReconnectWindow(), is(reconnectWindow));
  }

  @Test
  public void testConfigurationWithWildcards() {
    ServerConfiguration serverConfiguration =
        new ServerConfiguration(createServer(true), 100);

    assertThat(serverConfiguration.getTsaPort().getBind(), is(LOCALHOST));
    assertThat(serverConfiguration.getGroupPort().getBind(), is(LOCALHOST));
  }

  private static Server createServer(boolean wildcards) {
    String bindAddress = wildcards ? TCSocketAddress.WILDCARD_IP : LOCALHOST;

    Server server = new Server();
    server.setName(SERVER_NAME);
    server.setHost(LOCALHOST);
    server.setLogs(LOGS);
    server.setBind(LOCALHOST);

    BindPort tsaBind = new BindPort();
    tsaBind.setBind(bindAddress);
    tsaBind.setValue(TSA_PORT);

    BindPort groupBind = new BindPort();
    groupBind.setBind(bindAddress);
    groupBind.setValue(GROUP_PORT);

    server.setTsaPort(tsaBind);
    server.setTsaGroupPort(groupBind);

    return server;
  }
}