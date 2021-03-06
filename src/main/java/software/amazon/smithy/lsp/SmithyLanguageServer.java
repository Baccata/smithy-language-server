package software.amazon.smithy.lsp;

import java.io.File;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.MessageType;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

public class SmithyLanguageServer implements LanguageServer, LanguageClientAware {

  private Optional<LanguageClient> client = Optional.empty();

  private File workspaceRoot = null;

  private SmithyTextDocumentService tds = null;

  @Override
  public CompletableFuture<Object> shutdown() {
    return Utils.completableFuture(new Object());
  }

  @Override
  public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
    if (params.getRootUri() != null) {
      try {
        workspaceRoot = new File(new URI(params.getRootUri()));
      } catch (Exception e) {
        // TODO: handle exception
      }
    }

    ServerCapabilities capabilities = new ServerCapabilities();
    capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
    capabilities.setCodeActionProvider(false);
    capabilities.setDefinitionProvider(true);
    capabilities.setDeclarationProvider(true);
    capabilities.setCompletionProvider(new CompletionOptions(true, null));

    return Utils.completableFuture(new InitializeResult(capabilities));
  }

  @Override
  public void exit() {
    System.exit(0);

  }


  @Override
  public WorkspaceService getWorkspaceService() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TextDocumentService getTextDocumentService() {
    tds = new SmithyTextDocumentService(this.client);
    return this.tds;
  }

  @Override
  public void connect(LanguageClient client) {

    if(this.tds != null) {
      this.tds.setClient(client);
    }

    client.showMessage(new MessageParams(MessageType.Info, "Hello from smithy-language-server !"));
  }

}
