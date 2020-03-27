# Contributing

Contributions are very welcome. The following will provide some helpful guidelines.

## Follow the Code of Conduct

Contributors must follow the Code of Conduct outlined at [CODE-OF-CONDUCT.md](CODE-OF-CONDUCT.md).

## How to contribute

We love pull requests. Here is a quick guide:

1. You need to have a JDK (at least version 1.8) installed.
2. Fork the repo (see https://help.github.com/articles/fork-a-repo).
3. Create a new branch from master.
4. Ensure that you have a clean state by running `./gradlew clean build`.
5. Add your change together with a test (tests are not needed for refactorings and documentation changes).
6. Run `./gradlew clean build` again and ensure all tests are passing.
7. Create a Pull Request

### Commits

Commit messages should be clear and fully elaborate the context and the reason of a change.
If your commit refers to an issue, please post-fix it with the issue number, e.g.

```
Issue: #123
```

Furthermore, commits should be signed off according to the [DCO](DCO).

### Pull Requests

If your Pull Request resolves an issue, please add a respective line to the end, like

```
Resolves #123
```
