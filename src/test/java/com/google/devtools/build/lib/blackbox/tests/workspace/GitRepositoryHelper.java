// Copyright 2019 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.google.devtools.build.lib.blackbox.tests.workspace;

import com.google.devtools.build.lib.blackbox.framework.BlackBoxTestContext;
import com.google.devtools.build.lib.blackbox.framework.ProcessResult;
import java.nio.file.Path;

/**
 * Helper class for working with local git repository in tests. Should not be used outside ot tests.
 */
class GitRepositoryHelper {
  private final BlackBoxTestContext context;
  private final Path root;

  /**
   * Constructs the helper.
   *
   * @param context {@link BlackBoxTestContext} for running git process
   * @param root working directory for running git process, expected to be existing.
   */
  GitRepositoryHelper(BlackBoxTestContext context, Path root) {
    this.context = context;
    this.root = root;
  }

  /**
   * Calls 'git init' and 'git config' for specifying test user and email.
   *
   * @throws Exception related to the invocation of the external git process (like IOException or
   *     TimeoutException) or ProcessRunnerException if the process returned not expected return
   *     code.
   */
  void init() throws Exception {
    runGit("init");
    runGit("config", "user.email", "me@example.com");
    runGit("config", "user.name", "E X Ample");
  }

  /**
   * Recursively updates git index for all the files and directories under the working directory.
   *
   * @throws Exception related to the invocation of the external git process (like IOException or
   *     TimeoutException) or ProcessRunnerException if the process returned not expected return
   *     code.
   */
  void addAll() throws Exception {
    runGit("add", ".");
  }

  /**
   * Commits all staged changed.
   *
   * @param commitMessage commit message
   * @throws Exception related to the invocation of the external git process (like IOException or
   *     TimeoutException) or ProcessRunnerException if the process returned not expected return
   *     code.
   */
  void commit(String commitMessage) throws Exception {
    runGit("commit", "-m", commitMessage);
  }

  /**
   * Tags the HEAD commit.
   *
   * @param tagName tag name
   * @throws Exception related to the invocation of the external git process (like IOException or
   *     TimeoutException) or ProcessRunnerException if the process returned not expected return
   *     code.
   */
  void tag(String tagName) throws Exception {
    runGit("tag", tagName);
  }

  /**
   * Returns the HEAD's commit hash.
   *
   * @throws Exception related to the invocation of the external git process (like IOException or
   *     TimeoutException) or ProcessRunnerException if the process returned not expected return
   *     code.
   */
  String getHead() throws Exception {
    return runGit("rev-parse", "--short", "HEAD");
  }

  private String runGit(String... arguments) throws Exception {
    ProcessResult result = context.runBinary(root, "git", arguments);
    return result.outString();
  }
}
