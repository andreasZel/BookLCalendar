# Session Workflow

1. Read `Tasks.md` first, it holds the progress, decisions and the next task
2. Work on one task at a time on a `feat/<issue>-<slug>` or `fix/<issue>-<slug>` branch
3. Update the task's status and checklist in `Tasks.md` when starting and finishing
4. The `prepare-commit-msg` hook adds `[<issue>]` to commit messages, don't type it
5. Never commit `.env`, stage files by path
6. Issues and pull requests are managed with `node scripts/github.mjs`, `gh` is not installed
7. No `Co-Authored-By` or "Generated with Claude Code" lines in commits or pull requests

# Read Rules

1. Don't read node_modules folder, see the package.json or npm-shrinkwrap.json for dependencies

# Code Creation Rules

1. Functions parameters have to be inline, not wrapped
    example
    ```javascript
    //correct
    function add(num1, num2) {
    }
    //wrong
    function add(
        num1,
        num2
    ) {
    }
    ```
2. Inline Css has to be inline, not wrapped
    example
    ```javascript
    //correct
    <div style={{ width: '100%', padding: 2 }}>
    </div>
    //wrong
    <div style={{ 
        width: '100%',
        padding: 2
    }}>
    </div>
    ```
3. don't use backticks with <style> in jsx components
    example
    ```javascript
    //wrong
    <div>
        <style>
            `
            width: 100%; top: 1px, position: absolute;
            `
        </style>
    </div>
    ```
4. use minimal comments, max 12 words and try to avoid using them

5. don't use functions to render pages, write the jsx directly in the return, even when conditional rendering or returning early
    example 
    ```javascript
        //wrong
        const ExampleComp = () => {
            const renderPager = () => {
                if (totalPages <= 1) return null;

                if (totalPages <= 3) {
                    return (
                        <div className="common-carousell__pager">
                            {Array.from({ length: totalPages }, (_, i) => i).map((i) => (
                                <button type="button" key={i} className={`common-carousell__page-button${i === page ? " common-carousell__page-button--active" : ""}`} onClick={() => setPage(i)} aria-current={i === page ? "page" : undefined}>
                                    {i + 1}
                                </button>
                            ))}
                        </div>
                    );
                }

                return (
                    <div className="common-carousell__pager">
                    </div>
                );
            };

            return <div>{renderPager()}</div>;
        }
    ```

6. Local scoped types should be in the end of the components, globals in the /server/types.ts

7. If you declare more than one components in a file, make them seperate files and import them to the one that needs them. If you need to make a common place for helper components make a folder with files in it rather than one file

8. components should be exported inline rather after decleration
    example
    ```javascript
    // correct
    export const Foo = () => {
        return <></>
    }
    // wrong
    const Foo = () => {
        return <></>
    }

    export default Foo;
    ```
